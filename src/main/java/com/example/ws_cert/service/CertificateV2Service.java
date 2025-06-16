package com.example.ws_cert.service;

import com.example.ws_cert.utils.EjbTLSConnectionUtils;
import com.example.ws_cert.utils.HttpUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificateV2Service {
    private final EjbTLSConnectionUtils ejbTLSConnectionUtils;
    private final HttpUtils httpUtils;

    @Value("${ejbca.url}")
    private String ejbcaUrl;

    @Value("${ejbca.url.prefix-v2-certificate}")
    private String prefixUrl;

    private String caV1Url;

    @PostConstruct
    public void init() {
        caV1Url = ejbcaUrl + prefixUrl;
    }
}
