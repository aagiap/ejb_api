package com.example.ws_cert.dto.ejb.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmpRevocationRequest {
    private String password;
    private String subjectDn;
    private String issuerDn;
    private int revocationReason;
    private BigInteger serialNumber;
    private String serialNumberHex;
}
