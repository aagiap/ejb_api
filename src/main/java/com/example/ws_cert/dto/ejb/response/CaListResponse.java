package com.example.ws_cert.dto.ejb.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CaListResponse {
    private List<CertificateAuthority> certificate_authorities;
}
