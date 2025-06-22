package com.example.ws_cert.controller;

import com.example.ws_cert.constant.ApiSuccessCode;
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
        ApiSuccessCode apiSuccessCode = ApiSuccessCode.CREATED;
        return ApiResponse.<UserResponse>builder()
                .status(apiSuccessCode.getCode())
                .message(apiSuccessCode.getMessage())
                .response(userService.createUser(request))
                .build();
    }

    @GetMapping("/getAll")
    ApiResponse<List<UserResponse>> getUsers() {
        ApiSuccessCode apiSuccessCode = ApiSuccessCode.SUCCESS;
        return ApiResponse.<List<UserResponse>>builder()
                .status(apiSuccessCode.getCode())
                .message(apiSuccessCode.getMessage())
                .response(userService.getUsers())
                .build();
    }

    @GetMapping("/getMyInfo")
    ApiResponse<UserResponse> getMyInfo() {
        ApiSuccessCode apiSuccessCode = ApiSuccessCode.SUCCESS;
        return ApiResponse.<UserResponse>builder()
                .status(apiSuccessCode.getCode())
                .message(apiSuccessCode.getMessage())
                .response(userService.getMyInfo())
                .build();
    }

}
