package com.example.ws_cert.utils;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Component
public class HttpUtils {
    public HttpRequest buildHttpRequest(String url, String method, String body, Map<String, String> headers) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url));

        switch (method.toUpperCase()) {
            case "GET" -> builder.GET();
            case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body != null ? body : ""));
            case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(body != null ? body : ""));
            case "DELETE" -> builder.DELETE();
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        if (headers != null) {
            headers.forEach(builder::header);
        }

        return builder.build();
    }


    public HttpRequest build(String url, String method, String body) {
        return buildHttpRequest(url, method, body, Map.of("Content-Type", "application/json"));
    }

    public ResponseEntity<String> buildResponse(HttpResponse<String> response){
        return ResponseEntity.status(response.statusCode())
                .body(response.body());
    }
}
