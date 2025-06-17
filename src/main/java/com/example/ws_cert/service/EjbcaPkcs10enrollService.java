package com.example.ws_cert.service;

import com.example.ws_cert.entity.CertRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyStore;


@Service
public class EjbcaPkcs10enrollService {

      private String pkcsEndpoint = "https://localhost:8443/ejbca/ejbca-rest-api/v1/certificate/pkcs10enroll";
      private String keystoreFile = "D:\\\\tet7\\\\ejbca-ce-r9.0.0\\\\p12\\\\superadmin.p12";

    //private String pkcsEndpoint = "https://192.168.49.147:8443/ejbca/ejbca-rest-api/v1/certificate/pkcs10enroll";
    //private String pkcsEndpoint = "https://ejbca.local:8443/ejbca/ejbca-rest-api/v1/certificate/pkcs10enroll";
   // private String keystoreFile = "C:\\\\Users\\\\giap\\\\Desktop\\\\p12\\\\superadminubuntu.p12";
    private String keystorePassword = "ejbca";

    //private String truststoreFile = "keystore/truststoreC.p12";
    private String truststoreFile = "D:\\\\\\\\tet7\\\\\\\\ejbca-ce-r9.0.0\\\\\\\\p12\\\\\\\\truststore.p12";
    private String truststorePassword = "changeit";

    private final WebClient webClient;

    public EjbcaPkcs10enrollService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public String getCertResponse(CertRequest certRequest) {
        try {
            // Load client keystore (PKCS#12)
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream fis = new FileInputStream(keystoreFile)) {
                keyStore.load(fis, keystorePassword.toCharArray());
            }

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keystorePassword.toCharArray());

            // Load truststore from classpath
            KeyStore trustStore = KeyStore.getInstance("JKS");
//            try (InputStream trustStream = getClass().getClassLoader().getResourceAsStream(truststoreFile)) {
//                if (trustStream == null) {
//                    throw new FileNotFoundException("TrustStore file not found in classpath: " + truststoreFile);
//                }
//                trustStore.load(trustStream, truststorePassword.toCharArray());
//            }
            try (FileInputStream fis = new FileInputStream(truststoreFile)) {
                trustStore.load(fis, truststorePassword.toCharArray());
            }

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            // Setup SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            HttpClient client = HttpClient.newBuilder().sslContext(sslContext).build();

            String jsonRequest = String.format("""
                    {
                        "certificate_request": "%s",
                        "certificate_profile_name": "%s",
                        "end_entity_profile_name": "%s",
                        "certificate_authority_name": "%s",
                        "username": "%s",
                        "password": "%s",
                        "response_format": "%s"
                    }
                    """, certRequest.getCertificate_request(), certRequest.getCertificate_profile_name(), certRequest.getEnd_entity_profile_name(), certRequest.getCertificate_authority_name(), certRequest.getUsername(), certRequest.getPassword(), certRequest.getResponse_format());

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(pkcsEndpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (Exception e) {
            return "Error processing: " + e.getMessage() + "\nCause: " + e.getCause();
        }
    }


}
