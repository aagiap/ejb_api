package com.example.ws_cert.mapper;

import com.example.ws_cert.dto.request.UserCreationRequest;
import com.example.ws_cert.dto.response.UserResponse;
import com.example.ws_cert.entity.User;
import com.example.ws_cert.security.dto.request.SignUpRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    User toUser(SignUpRequest request);
    UserResponse toUserResponse(User user);
}
