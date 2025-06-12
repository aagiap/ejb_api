package com.example.ws_cert.service;

import com.example.ws_cert.dto.ejb.request.GetLastestCrlRequest;
import com.example.ws_cert.dto.ejb.response.CaStatusResponse;
import com.example.ws_cert.dto.ejb.response.GetLastestcrlResponse;
import com.example.ws_cert.utils.EjbTLSConnectionUtils;
import com.example.ws_cert.utils.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class CaV1Service {
    private final EjbTLSConnectionUtils ejbTLSConnectionUtils;
    private final HttpUtils httpUtils;

    @Value("${ejbca.url}")
    private String ejbcaUrl;

    @Value("${ejbca.url.prefix-v1-ca}")
    private String prefixUrl;

    private String caV1Url;

    @PostConstruct
    public void init() {
        caV1Url = ejbcaUrl + prefixUrl;
    }


    public CaStatusResponse getCaStatus() throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();

        String url = caV1Url + "/status";

        HttpRequest request = httpUtils.build(url, "GET", null);

        HttpClient client = HttpClient.newBuilder().sslContext(sslContext).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), CaStatusResponse.class);
    }

    public String certDownload(String subjectDn) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();
        String url = caV1Url + "/" + subjectDn + "/certificate/download";
        HttpRequest request = httpUtils.build(url, "GET", null);

        HttpClient client = HttpClient.newBuilder().sslContext(sslContext).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public GetLastestcrlResponse getLastestCrl(String issuer_dn, boolean deltaCrl, Integer crlPartitionIndex) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();
        String url = caV1Url + "/" + issuer_dn + "/getLatestCrl" +
                "?deltaCrl=" + deltaCrl +
                "&crlPartitionIndex=" + crlPartitionIndex;
        HttpRequest request = httpUtils.build(url, "GET", null);

        HttpClient client = HttpClient.newBuilder().sslContext(sslContext).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), GetLastestcrlResponse.class);
    }

}
