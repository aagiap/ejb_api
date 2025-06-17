package com.example.ws_cert.service;

import com.example.ws_cert.utils.EjbTLSConnectionUtils;
import com.example.ws_cert.utils.HttpUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.net.http.HttpRequest;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CertificateV2Service {
    private final EjbTLSConnectionUtils ejbTLSConnectionUtils;
    private final HttpUtils httpUtils;

    @Value("${ejbca.url}")
    private String ejbcaUrl;

    @Value("${ejbca.url.prefix-v2-certificate}")
    private String prefixUrl;

    private String certV2Url;

    @PostConstruct
    public void init() {
        certV2Url = ejbcaUrl + prefixUrl;
    }

    public Map<String, Object> markKeyRecovery(String issuer_dn, String certificate_serial_number) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();
        String url = certV2Url + "/" + issuer_dn + "/" + certificate_serial_number + "/" + "keyrecovery";
        HttpRequest request = httpUtils.build(url, "PUT", null);
        return httpUtils.getStringObjectMap(sslContext, request);
    }
}
