package com.example.ws_cert.dto.ejb.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateSearchCriteriaV1 {
    private String property;
    private String value;
    private String operation;
}
