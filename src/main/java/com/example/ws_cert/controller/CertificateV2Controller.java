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

    // Returns status, API version and EJBCA version.
    @GetMapping("/status")
    public ApiResponse<Map<String, Object>> getCaStatus() {
        return certificateV2Service.getStatus();
    }

    // Make certificate for a key recovery.
    @PutMapping("/key-recovery/{issuer_dn}/{certificate_serial_number}")
    public ApiResponse<Map<String, Object>> markKeyRecovery(@PathVariable String issuer_dn,
                                                            @PathVariable String certificate_serial_number) {
        return certificateV2Service.markKeyRecovery(issuer_dn, certificate_serial_number);
    }

    // Get certificate Profile info
    @GetMapping("/cert-profile/{profile_name}")
    public ApiResponse<Map<String, Object>> getCertificateProfile(@PathVariable String profile_name) {
        return certificateV2Service.getCertProfile(profile_name);
    }

    // Get the quantity of rather total issued or active certificates.
    @GetMapping("/cert-count")
    public ApiResponse<Map<String, Object>> countActiveCert(@RequestParam(required = false) Boolean isActive) {
        return certificateV2Service.countActiveCert(isActive);
    }

    // Insert as many search criteria as needed. A reference about allowed values for criteria could be found below, under SearchCertificateCriteriaRestRequestV2 model.
    // Use -1 for current_page to get total number of certificate for the request criteria.
    @PostMapping("/search")
    public ApiResponse<Map<String, Object>> searchCertificates(@RequestBody CertificateSearchRequestV2 certificateSearchRequestV2) {
        return certificateV2Service.searchCertificate(certificateSearchRequestV2);
    }
}
