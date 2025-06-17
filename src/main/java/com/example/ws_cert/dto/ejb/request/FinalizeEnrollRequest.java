package com.example.ws_cert.dto.ejb.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalizeEnrollRequest {
    private String password;
    private String response_format;
    private String key_alg;
    private Integer key_spec;
}
