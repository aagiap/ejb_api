package com.example.ws_cert.utils;


import com.example.ws_cert.dto.response.ApiResponse;
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
                throw new RuntimeException("Failed to serialize body to JSON", e);
            }
        }

        switch (method.toUpperCase()) {
            case "GET" -> builder.GET();
            case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(jsonBody));
            case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(jsonBody));
            case "DELETE" -> builder.DELETE();
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        if (headers != null) {
            headers.forEach(builder::header);
        }

        return builder.build();
    }

    public HttpRequest build(String url, String method, Object body) {
        return buildHttpRequest(url, method, body, Map.of("Content-Type", "application/json"));
    }


    public ApiResponse<Map<String, Object>> getStringObjectMap(SSLContext sslContext, HttpRequest request) throws java.io.IOException, InterruptedException {
        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
        HttpClient client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        apiResponse.setResponse(mapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {}));
        apiResponse.setMessage(getResponseMessage(response.statusCode()));
        apiResponse.setStatus(response.statusCode());
        return apiResponse;
    }

    private String getResponseMessage(Integer statusCode) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 202 -> "Accepted";
            case 400 -> "Bad Request";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 409 -> "Conflict";
            case 413 -> "Payload Too Large";
            case 422 -> "Unprocessable Entity";
            case 500 -> "Internal Server Error";
            case 503 -> "Service Unavailable";
            default -> "Unknown Status Code";
        };
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
