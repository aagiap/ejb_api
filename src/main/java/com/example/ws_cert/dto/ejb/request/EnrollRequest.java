package com.example.ws_cert.dto.ejb.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollRequest {
//    private String subject_dn;
    private String certificate_request;
    private String certificate_profile_name;
    private String end_entity_profile_name;
    private String certificate_authority_name;
    private String username;
    private String password;
    private String response_format;
    private boolean include_chain;
}
