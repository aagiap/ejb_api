package com.example.ws_cert.dto.ejb.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRequest {
    private String password;
    private String certificate_authority_name;
    private Boolean include_chain;
    private String certificate_request;
    private String username;
}
