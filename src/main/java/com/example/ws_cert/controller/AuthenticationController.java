package com.example.ws_cert.controller;

import com.example.ws_cert.dto.request.LoginRequest;
import com.example.ws_cert.dto.response.LoginResponse;
import com.example.ws_cert.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
//    private final JWTUtils jwtUtils;
//
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
//        return ResponseEntity.ok(jwtUtils.authenticate(request));
//    }
}
