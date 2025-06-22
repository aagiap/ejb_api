package com.example.ws_cert.utils;

import com.example.ws_cert.exception.AppException;
import com.example.ws_cert.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;

@Component
public class EjbTLSConnectionUtils {

    @Value("${ejbca.keystore.file}")
    private String keystoreFileUrl;

    @Value("${ejbca.keystore.password}")
    private String keystorePassword;

    @Value("${ejbca.keystore.type}")
    private String keystoreType;

    @Value("${ejbca.truststore.file}")
    private String truststoreFileUrl;

    @Value("${ejbca.truststore.password}")
    private String truststorePassword;

    @Value("${ejbca.truststore.type}")
    private String truststoreType;


    public SSLContext createSSLContext() {
        try {
            // Load client keystore
            KeyStore keyStore = loadStore(keystoreFileUrl, keystoreType, keystorePassword);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keystorePassword.toCharArray());

            // Load truststore (JKS or PKCS12 depending on your file)
            KeyStore trustStore = loadStore(truststoreFileUrl, truststoreType, truststorePassword);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            // Initialize SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            return sslContext;
        } catch (Exception e) {
            throw new AppException(ErrorCode.FAILED_TO_CREATE_SSL_CONTEXT_WITH_EJBCA);
        }

    }

    private KeyStore loadStore(String storePath, String storeType, String password) {
        try {
        KeyStore keyStore = KeyStore.getInstance(storeType);
        if (storePath.startsWith("classpath:")) {
            String classpathResource = storePath.substring("classpath:".length());
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(classpathResource)) {
                if (inputStream == null) {
                    throw new AppException(ErrorCode.KEYSTORE_NOT_FOUND);
                }
                keyStore.load(inputStream, password.toCharArray());
            } catch (Exception e) {
                throw new AppException(ErrorCode.FAILED_TO_LOAD_KEY_STORE_FROM_FILE);
            }
        } else {
            try (FileInputStream fileInputStream = new FileInputStream(storePath)) {
                keyStore.load(fileInputStream, password.toCharArray());
            } catch (Exception e) {
                throw new AppException(ErrorCode.FAILED_TO_LOAD_KEY_STORE_FROM_FILE);
            }
        }
        return keyStore;
        } catch (KeyStoreException exception){
            throw new AppException(ErrorCode.FAILED_TO_LOAD_KEYSTORE);
        }
    }
}
