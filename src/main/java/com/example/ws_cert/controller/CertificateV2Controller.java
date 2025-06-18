package com.example.ws_cert.controller;

import com.example.ws_cert.dto.ejb.request.CertificateSearchRequestV2;
import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.service.CertificateV2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cert-v2")
@RequiredArgsConstructor
public class CertificateV2Controller {
    private final CertificateV2Service certificateV2Service;

    @GetMapping("/status")
    public ApiResponse<Map<String, Object>> getCaStatus() throws Exception {
        return certificateV2Service.getStatus();
    }

    @PutMapping("/key-recovery/{issuer_dn}/{certificate_serial_number}")
    public ApiResponse<Map<String, Object>> markKeyRecovery(@PathVariable String issuer_dn,
                                                            @PathVariable String certificate_serial_number) throws Exception {
        return certificateV2Service.markKeyRecovery(issuer_dn, certificate_serial_number);
    }

    @GetMapping("/cert-profile/{profile_name}")
    public ApiResponse<Map<String, Object>> getCertificateProfile(@PathVariable String profile_name) throws Exception {
        return certificateV2Service.getCertProfile(profile_name);
    }

    @GetMapping("/cert-count")
    public ApiResponse<Map<String, Object>> countActiveCert(@RequestParam(required = false) Boolean isActive) throws Exception {
        return certificateV2Service.countActiveCert(isActive);
    }

    @PostMapping("/search")
    public ApiResponse<Map<String, Object>> searchCertificates(@RequestBody CertificateSearchRequestV2 certificateSearchRequestV2) throws Exception {
        return certificateV2Service.searchCertificate(certificateSearchRequestV2);
    }
}
