package com.example.ws_cert.service;

import com.example.ws_cert.dto.ejb.request.GetLastestCrlRequest;
import com.example.ws_cert.dto.ejb.response.*;
import com.example.ws_cert.utils.EjbTLSConnectionUtils;
import com.example.ws_cert.utils.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.net.ssl.SSLContext;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

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


//    public CaStatusResponse getCaStatus() throws Exception {
//        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();
//
//        String url = caV1Url + "/status";
//
//        HttpRequest request = httpUtils.build(url, "GET", null);
//
//        HttpClient client = HttpClient.newBuilder().sslContext(sslContext).build();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readValue(response.body(), CaStatusResponse.class);
//    }



//    public Map<String, Object> getCaStatus() throws Exception {
//        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();
//
//        String url = caV1Url + "/status";
//        HttpRequest request = httpUtils.build(url, "GET", null);
//        HttpClient client = HttpClient.newBuilder().sslContext(sslContext).build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
//    }


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

    public CaListResponse getListCa(boolean includeExternal) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();
        String url = caV1Url + "/" + includeExternal;
        HttpRequest request = httpUtils.build(url, "GET", null);

        HttpClient client = HttpClient.newBuilder().sslContext(sslContext).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), CaListResponse.class);
    }

    public CreateCrlResponse createCrl(String issuer_dn, boolean deltacrl) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();
        String url = caV1Url + "/" + issuer_dn + "?deltacrl=" +deltacrl;
        HttpRequest request = httpUtils.build(url, "POST", null);

        HttpClient client = HttpClient.newBuilder().sslContext(sslContext).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), CreateCrlResponse.class);
    }


}
