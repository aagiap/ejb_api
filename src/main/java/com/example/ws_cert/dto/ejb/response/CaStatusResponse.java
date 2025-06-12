package com.example.ws_cert.dto.ejb.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CaStatusResponse {
    private String version;
    private String status;
    private String revision;
}
