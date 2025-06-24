package com.example.ws_cert.utils;


import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.exception.AppException;
import com.example.ws_cert.constant.ErrorCode;
import com.example.ws_cert.constant.EjbcaStatusCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HttpUtils {


    private final ObjectMapper objectMapper = new ObjectMapper();

    public HttpRequest buildHttpRequest(String url, String method, Object body, Map<String, String> headers) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url));

        String jsonBody = "";
        if (body != null) {
            try {
                jsonBody = objectMapper.writeValueAsString(body);
            } catch (Exception e) {
                throw new AppException(ErrorCode.FAILED_TO_SERIALIZE_BODY_TO_JSON);
            }
        }

        switch (method.toUpperCase()) {
            case "GET" -> builder.GET();
            case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(jsonBody));
            case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(jsonBody));
            case "DELETE" -> builder.DELETE();
            default -> throw new AppException(ErrorCode.UNSUPPORTED_HTTP_METHOD);
        }

        if (headers != null) {
            headers.forEach(builder::header);
        }

        return builder.build();
    }

    public HttpRequest build(String url, String method, Object body) {
        return buildHttpRequest(url, method, body, Map.of("Content-Type", "application/json"));
    }


    public ApiResponse<Map<String, Object>> getStringObjectMap(SSLContext sslContext, HttpRequest request) {
        try {
            ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
            HttpClient client = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            apiResponse.setResponse(mapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {
            }));

            EjbcaStatusCode ejbcaStatus = EjbcaStatusCode.fromCode(response.statusCode());
                apiResponse.setCode(ejbcaStatus.getCode());
                apiResponse.setMessage(ejbcaStatus.getMessage());

            return apiResponse;
        } catch (Exception e) {
            throw new AppException(ErrorCode.FAILED_TO_GET_RESPONSE);
        }
    }


    public String appendQueryParams(String baseUrl, Map<String, String> params) {
        if (params == null || params.isEmpty()) return baseUrl;

        String queryString = params.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isBlank())
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                        URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        if (queryString.isEmpty()) return baseUrl;

        return baseUrl + (baseUrl.contains("?") ? "&" : "?") + queryString;
    }

    public String encodePathSegment(String segment) {
        return segment.replace(" ", "%20")
                .replace("+", "%2B")
                .replace("/", "%2F")
                .replace("=", "=");
    }

//public String appendQueryParams(String baseUrl, Map<String, String> params) {
//    if (params == null || params.isEmpty()) return baseUrl;
//
//    String queryString = params.entrySet().stream()
//            .filter(entry -> entry.getValue() != null && !entry.getValue().isBlank())
//            .map(entry -> entry.getKey() + "=" + entry.getValue())
//            .collect(Collectors.joining("&"));
//
//    if (queryString.isEmpty()) return baseUrl;
//
//    return baseUrl + (baseUrl.contains("?") ? "&" : "?") + queryString;
//}

}
