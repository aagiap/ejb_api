package com.example.ws_cert.controller;

import com.example.ws_cert.dto.request.UserCreationRequest;
import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.dto.response.UserResponse;
import com.example.ws_cert.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;



    @PostMapping("/create")
    ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder().response(userService.createUser(request)).build();
    }

    @GetMapping("/getAll")
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder().response(userService.getUsers()).build();
    }

    @GetMapping("/getMyInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder().response(userService.getMyInfo()).build();
    }

}
