package com.example.ws_cert.security.controller;

import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.dto.response.UserResponse;
import com.example.ws_cert.security.dto.request.LoginRequest;
import com.example.ws_cert.security.dto.request.SignUpRequest;
import com.example.ws_cert.security.dto.response.LoginResponse;
import com.example.ws_cert.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            LoginResponse loginResponse = authService.login(loginRequest);
            return ApiResponse.<LoginResponse>builder().response(loginResponse).build();
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PostMapping("/signup")
    public ApiResponse<UserResponse> signup(@RequestBody SignUpRequest signUpRequest){
        UserResponse userResponse = authService.signUp(signUpRequest);
        return ApiResponse.<UserResponse>builder().response(userResponse).build();
    }
}
