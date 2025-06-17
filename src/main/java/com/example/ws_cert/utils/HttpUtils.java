package com.example.ws_cert.utils;


import com.example.ws_cert.dto.response.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.EntityBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

@Slf4j
@Component
public class HttpUtils {
//    public HttpRequest buildHttpRequest(String url, String method, String body, Map<String, String> headers) {
//        HttpRequest.Builder builder = HttpRequest.newBuilder()
//                .uri(URI.create(url));
//
//        switch (method.toUpperCase()) {
//            case "GET" -> builder.GET();
//            case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body != null ? body : ""));
//            case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(body != null ? body : ""));
//            case "DELETE" -> builder.DELETE();
//            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
//        }
//
//        if (headers != null) {
//            headers.forEach(builder::header);
//        }
//
//        return builder.build();
//    }
//
//
//    public HttpRequest build(String url, String method, String body) {
//        return buildHttpRequest(url, method, body, Map.of("Content-Type", "application/json"));
//    }
private final ObjectMapper objectMapper = new ObjectMapper(); // dùng 1 lần duy nhất

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

    public ResponseEntity<String> buildResponse(HttpResponse<String> response){
        return ResponseEntity.status(response.statusCode())
                .body(response.body());
    }

//    // Multipart request (for file upload)
//    public String sendMultipartRequest(String url, File file, String fileFieldName) throws IOException {
//        HttpPost post = new HttpPost(url);
//
//        HttpEntity multipart = EntityBuilder.create()
//                .setContentType(ContentType.MULTIPART_FORM_DATA)
//                .setFile(file)
//                .build();
//
//        post.setEntity(multipart);
//
//        return httpClient.execute(post, response ->
//                new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8)
//        );
//    }

//    public Map<String, Object> getStringObjectMap(SSLContext sslContext, HttpRequest request) throws java.io.IOException, InterruptedException {
//        HttpClient client = HttpClient.newBuilder()
//                .sslContext(sslContext)
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
//    }

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
        switch (statusCode){
            case 200: return "OK";
            case 201: return "Created";
            case 202: return "Accepted";
            case 400: return "Bad Request";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 409: return "Conflict";
            case 413: return "Payload Too Large";
            case 422: return "Unprocessable Entity";
            case 500: return "Internal Server Error";
            case 503: return "Service Unavailable";
        }
        return "Unknown Status Code: ";
    }
}
