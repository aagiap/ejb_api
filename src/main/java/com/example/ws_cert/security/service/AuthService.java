package com.example.ws_cert.security.service;

import com.example.ws_cert.constant.UserRole;
import com.example.ws_cert.dto.response.UserResponse;
import com.example.ws_cert.entity.Role;
import com.example.ws_cert.entity.User;
import com.example.ws_cert.exception.AppException;
import com.example.ws_cert.constant.ErrorCode;
import com.example.ws_cert.mapper.UserMapper;
import com.example.ws_cert.repository.RoleRepository;
import com.example.ws_cert.repository.UserRepository;
import com.example.ws_cert.security.dto.request.LoginRequest;
import com.example.ws_cert.security.dto.request.SignUpRequest;
import com.example.ws_cert.security.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        String token = jwtService.generateToken(loginRequest.getUsername());

        return LoginResponse.builder().token(token).build();
    }

    public UserResponse signUp(SignUpRequest signUpRequest) {

        User user = userMapper.toUser(signUpRequest);

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(UserRole.ROLE_USER)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        roles.add(role);
        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.DATA_INTEGRITY_VIOLATION);
        }
        return userMapper.toUserResponse(user);
    }

    public boolean authenticate(LoginRequest request) {
        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.WRONG_PASSWORD);

        return true;

    }
}
