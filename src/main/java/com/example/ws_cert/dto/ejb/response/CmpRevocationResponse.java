package com.example.ws_cert.dto.ejb.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CmpRevocationResponse {
    private Integer code;
    private String message;
}
