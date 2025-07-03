package com.example.ws_cert.dto.ejb.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollCertificateResponse {
    private Integer code;
    private String subject_dn;
    private String certificate_base64;
}
