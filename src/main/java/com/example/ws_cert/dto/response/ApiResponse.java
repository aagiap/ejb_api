package com.example.ws_cert.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ApiResponse<T> {
    private Integer status;
    private String message;
    private T response;
}
