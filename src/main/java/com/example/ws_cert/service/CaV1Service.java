package com.example.ws_cert.service;

import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.exception.AppException;
import com.example.ws_cert.constant.ErrorCode;
import com.example.ws_cert.utils.HttpUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CaV1Service {
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


    public ApiResponse<Map<String, Object>> getCaStatus() {
        String url = caV1Url + "/status";
        HttpRequest request = httpUtils.build(url, "POST", null);
        return httpUtils.getStringObjectMap(request);
    }


    public ApiResponse<Map<String, Object>> certDownload(String subjectDn) {
        String url = caV1Url + "/" + subjectDn + "/certificate/download";
        HttpRequest request = httpUtils.build(url, "GET", null);

        return httpUtils.getStringObjectMap(request);
    }

    public ApiResponse<Map<String, Object>> getLastestCrl(String issuer_dn, boolean deltaCrl, Integer crlPartitionIndex) {
        String url = caV1Url + "/" + issuer_dn + "/getLatestCrl";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("deltaCrl", String.valueOf(deltaCrl));
        queryParams.put("crlPartitionIndex", String.valueOf(crlPartitionIndex));
        url = httpUtils.appendQueryParams(url, queryParams);
        HttpRequest request = httpUtils.build(url, "GET", null);
        return httpUtils.getStringObjectMap(request);
    }


    //Returns the Response containing the list of CAs with general information per CA as Json
    public ApiResponse<Map<String, Object>> getListCa(boolean includeExternal) {
        String url = caV1Url + "?includeExternal=" + includeExternal;
        HttpRequest request = httpUtils.build(url, "GET", null);

        return httpUtils.getStringObjectMap(request);
    }


    public ApiResponse<Map<String, Object>> importCrl(String issuerDn, MultipartFile crlFile) {

        String boundary = "----JavaMultipartBoundary" + System.currentTimeMillis();

        String partitionIndex = "0";

        String crlPartitionIndexPart = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"crlPartitionIndex\"\r\n\r\n" +
                partitionIndex + "\r\n";

        String filePartHeader = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"crlFile\"; filename=\"" + crlFile.getOriginalFilename() + "\"\r\n" +
                "Content-Type: application/octet-stream\r\n\r\n";

        String filePartFooter = "\r\n--" + boundary + "--\r\n";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            outputStream.write(crlPartitionIndexPart.getBytes(StandardCharsets.UTF_8));
            outputStream.write(filePartHeader.getBytes(StandardCharsets.UTF_8));
            outputStream.write(crlFile.getBytes());
            outputStream.write(filePartFooter.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new AppException(ErrorCode.FAILED_TO_READ_CRL_FILE);
        }
        byte[] requestBody = outputStream.toByteArray();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(caV1Url + "/" + URLEncoder.encode(issuerDn, StandardCharsets.UTF_8) + "/importcrl"))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .header("Accept", "*/*")
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                .build();

        return httpUtils.getStringObjectMap(request);
    }

    public ApiResponse<Map<String, Object>> createCrl(String issuer_dn, boolean deltacrl) {
        String url = caV1Url + "/" + issuer_dn + "/createcrl" + "?deltacrl=" + deltacrl;
        HttpRequest request = httpUtils.build(url, "POST", null);

        return httpUtils.getStringObjectMap(request);
    }


}
