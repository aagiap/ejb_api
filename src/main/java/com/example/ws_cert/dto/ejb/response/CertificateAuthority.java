package com.example.ws_cert.dto.ejb.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CertificateAuthority {
    private long id;
    private String name;
    private String subject_dn;
    private String issuer_dn;
    private ZonedDateTime expiration_date;
    private boolean external;
}
