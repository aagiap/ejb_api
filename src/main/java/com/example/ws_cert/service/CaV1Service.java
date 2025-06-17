package com.example.ws_cert.service;

import com.example.ws_cert.dto.ejb.request.GetLastestCrlRequest;
import com.example.ws_cert.dto.ejb.response.*;
import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.utils.EjbTLSConnectionUtils;
import com.example.ws_cert.utils.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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



    public ApiResponse<Map<String, Object>> getCaStatus() throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();

        String url = caV1Url + "/status";
        HttpRequest request = httpUtils.build(url, "GET", null);
        return httpUtils.getStringObjectMap(sslContext, request);
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

//    public CaListResponse getListCa(boolean includeExternal) throws Exception {
//        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();
//        String url = caV1Url + "/" + includeExternal;
//        HttpRequest request = httpUtils.build(url, "GET", null);
//
//        HttpClient client = HttpClient.newBuilder().sslContext(sslContext).build();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readValue(response.body(), CaListResponse.class);
//    }

    //Returns the Response containing the list of CAs with general information per CA as Json
    public ApiResponse<Map<String, Object>> getListCa(boolean includeExternal) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();
        String url = caV1Url + "?includeExternal=" + includeExternal;
        HttpRequest request = httpUtils.build(url, "GET", null);

        return httpUtils.getStringObjectMap(sslContext, request);
    }




public ApiResponse<Map<String, Object>> importCrl(String issuerDn, MultipartFile crlFile) throws Exception {
    SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();

    String boundary = "----JavaMultipartBoundary" + System.currentTimeMillis();

    String partitionIndex = "0"; // Dùng 0 nếu bạn không phân mảnh CRL

    String crlPartitionIndexPart = "--" + boundary + "\r\n" +
            "Content-Disposition: form-data; name=\"crlPartitionIndex\"\r\n\r\n" +
            partitionIndex + "\r\n";

    String filePartHeader = "--" + boundary + "\r\n" +
            "Content-Disposition: form-data; name=\"crlFile\"; filename=\"" + crlFile.getOriginalFilename() + "\"\r\n" +
            "Content-Type: application/octet-stream\r\n\r\n";

    String filePartFooter = "\r\n--" + boundary + "--\r\n";

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputStream.write(crlPartitionIndexPart.getBytes(StandardCharsets.UTF_8));
    outputStream.write(filePartHeader.getBytes(StandardCharsets.UTF_8));
    outputStream.write(crlFile.getBytes());
    outputStream.write(filePartFooter.getBytes(StandardCharsets.UTF_8));

    byte[] requestBody = outputStream.toByteArray();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(caV1Url + "/" + URLEncoder.encode(issuerDn, StandardCharsets.UTF_8) + "/importcrl"))
            .header("Content-Type", "multipart/form-data; boundary=" + boundary)
            .header("Accept", "*/*")
            .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
            .build();

    return httpUtils.getStringObjectMap(sslContext, request);
}




    public ApiResponse<Map<String, Object>> createCrl(String issuer_dn, boolean deltacrl) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();
        String url = caV1Url + "/" + issuer_dn + "/createcrl" + "?deltacrl=" + deltacrl;
        HttpRequest request = httpUtils.build(url, "POST", null);

        return httpUtils.getStringObjectMap(sslContext, request);
    }


}
