package com.example.ws_cert.service;

import com.example.ws_cert.dto.request.LoginRequest;
import com.example.ws_cert.dto.response.LoginResponse;
import com.example.ws_cert.repository.UserRepository;
import com.example.ws_cert.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    public LoginResponse authenticate(LoginRequest request) {
        var user = userRepository
                .findByUserName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new RuntimeException("Invalid credentials");

        var token = jwtUtils.generateToken(user);

        return LoginResponse.builder().token(token).build();
    }
}
