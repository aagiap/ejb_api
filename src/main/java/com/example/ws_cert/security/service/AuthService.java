package com.example.ws_cert.security.service;

import com.example.ws_cert.constant.UserRole;
import com.example.ws_cert.dto.response.UserResponse;
import com.example.ws_cert.entity.Role;
import com.example.ws_cert.entity.User;
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
//        User user = userRepository.findByUserName(loginRequest.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found with username: " + loginRequest.getUsername()));
//        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid password for user: " + loginRequest.getUsername());
//        }
        String token = jwtService.generateToken(loginRequest.getUsername());

        return LoginResponse.builder().token(token).build();
    }

    public UserResponse signUp(SignUpRequest signUpRequest) {
        User user = userMapper.toUser(signUpRequest);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(UserRole.ROLE_USER);
        roles.add(role);
        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new RuntimeException("User already exists", exception);
        }

        return userMapper.toUserResponse(user);
    }
}
