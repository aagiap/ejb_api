package com.example.ws_cert.dto.ejb.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmpIrRequest {
    private String password;
    private String subjectDn;
    private String issueDn;
    private Date notBefore;
    private Date notAfter;
}
