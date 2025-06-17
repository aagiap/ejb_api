package com.example.ws_cert.controller;

import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.service.CertificateV1Service;
import com.example.ws_cert.service.CertificateV2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cert-v1")
@RequiredArgsConstructor
public class CertificateV2Controller {
    private final CertificateV2Service certificateV2Service;

    @PutMapping("/revocationstatus/{issuer_dn}/{certificate_serial_number}")
    public ApiResponse<Map<String, Object>> markKeyRecovery(@PathVariable String issuer_dn, @PathVariable String certificate_serial_number) throws Exception {
        return certificateV2Service.markKeyRecovery(issuer_dn, certificate_serial_number);
    }
}
