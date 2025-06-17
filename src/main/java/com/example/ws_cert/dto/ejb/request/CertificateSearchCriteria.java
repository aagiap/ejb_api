package com.example.ws_cert.dto.ejb.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateSearchCriteria {
    private String property;
    private String value;
    private String operation;
}
