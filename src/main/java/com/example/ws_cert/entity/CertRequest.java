package com.example.ws_cert.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertRequest {
    private String certificate_request;
    private String certificate_profile_name;
    private String end_entity_profile_name;
    private String certificate_authority_name;
    private String username;
    private String password;
    private String response_format;


}
