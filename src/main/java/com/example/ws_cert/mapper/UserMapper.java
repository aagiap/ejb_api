package com.example.ws_cert.mapper;

import com.example.ws_cert.dto.request.UserCreationRequest;
import com.example.ws_cert.dto.response.UserResponse;
import com.example.ws_cert.entity.User;
import com.example.ws_cert.security.dto.request.SignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toUser(SignUpRequest request);

    UserResponse toUserResponse(User user);
}
