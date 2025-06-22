package com.example.ws_cert.service;


import com.example.ws_cert.constant.UserRole;
import com.example.ws_cert.dto.request.UserCreationRequest;
import com.example.ws_cert.dto.response.UserResponse;
import com.example.ws_cert.entity.Role;
import com.example.ws_cert.entity.User;
import com.example.ws_cert.exception.AppException;
import com.example.ws_cert.exception.ErrorCode;
import com.example.ws_cert.mapper.UserMapper;
import com.example.ws_cert.repository.RoleRepository;
import com.example.ws_cert.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(UserRole.valueOf(request.getRole()))
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INTEGRITY_VIOLATION));

        roles.add(role);
        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        }  catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.DATA_INTEGRITY_VIOLATION);
        }

        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }


//    public void deleteUser(Integer userId) {
//        userRepository.deleteById(userId);
//    }


    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }




//    public UserResponse getUser(String id) {
//        return userMapper.toUserResponse(
//                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
//    }





}
