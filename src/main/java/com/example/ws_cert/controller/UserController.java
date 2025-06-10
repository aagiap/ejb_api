package com.example.ws_cert.controller;

import com.example.ws_cert.dto.request.UserCreationRequest;
import com.example.ws_cert.dto.response.UserResponse;
import com.example.ws_cert.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/create")
    ResponseEntity<UserResponse> createUser(@RequestBody UserCreationRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/getAll")
    ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

}
