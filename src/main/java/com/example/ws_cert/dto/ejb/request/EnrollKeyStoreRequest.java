package com.example.ws_cert.dto.ejb.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollKeyStoreRequest {
    private String password;
    private String key_alg;
    private Integer key_spec;
    private String username;
}
