package com.example.ws_cert.controller;

import com.example.ws_cert.dto.ejb.request.*;
import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.service.CertificateV1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cert-v1")
@RequiredArgsConstructor
public class CertificateV1Controller {
    private final CertificateV1Service certificateV1Service;

    @GetMapping("/status")
    public ApiResponse<Map<String, Object>> getCaStatus() throws Exception {
        return ApiResponse.<Map<String, Object>>builder().response(certificateV1Service.getStatus()).build();
    }

    @GetMapping("/expired")
    public ApiResponse<Map<String, Object>> getExpiredCert(@RequestParam Integer days,@RequestParam Integer offset, @RequestParam Integer maxNumberOfResults) throws Exception {
        return ApiResponse.<Map<String, Object>>builder().response(certificateV1Service.getExpiredCerts(days, offset, maxNumberOfResults)).build();
    }

    @GetMapping("/revocationstatus/{issuer_dn}/{certificate_serial_number}")
    public ApiResponse<Map<String, Object>> getRevocationStatus(@PathVariable String issuer_dn, @PathVariable String certificate_serial_number) throws Exception {
        return ApiResponse.<Map<String, Object>>builder().response(certificateV1Service.checkRevocationStatus(issuer_dn, certificate_serial_number)).build();
    }

    @GetMapping("/revoke/{issuer_dn}/{certificate_serial_number}")
    public ApiResponse<Map<String, Object>> revoke(@PathVariable String issuer_dn, @PathVariable String certificate_serial_number) throws Exception {
        return ApiResponse.<Map<String, Object>>builder().response(certificateV1Service.revoke(issuer_dn, certificate_serial_number)).build();
    }

    @PostMapping("/search")
    public ApiResponse<Map<String, Object>> searchCertificate(@RequestBody Map<String, Object> searchCertificateRequest) throws Exception {
        return ApiResponse.<Map<String, Object>>builder().response(certificateV1Service.searchCertificate(searchCertificateRequest)).build();
    }
//    @PostMapping("/search")
//    public ApiResponse<Map<String, Object>> searchCertificate(@RequestBody SearchCertificateRequest searchCertificateRequest) throws Exception {
//        return ApiResponse.<Map<String, Object>>builder().response(certificateV1Service.searchCertificate(searchCertificateRequest)).build();
//    }

    @PostMapping("/enroll")
    public ApiResponse<Map<String, Object>> enrollCertificate(@RequestBody EnrollRequest enrollRequest) throws Exception {
        return ApiResponse.<Map<String, Object>>builder().response(certificateV1Service.enrollCertificate(enrollRequest)).build();
    }

    @PostMapping("/finalize/{request_id}")
    public ApiResponse<Map<String, Object>> finalizeEnroll(@PathVariable String request_id,@RequestBody FinalizeEnrollRequest finalizeEnrollRequest) throws Exception {
        return ApiResponse.<Map<String, Object>>builder().response(certificateV1Service.finalizeEnrollCertificate(request_id,finalizeEnrollRequest)).build();
    }

    @PostMapping("/enrollKeyStore")
    public ApiResponse<Map<String, Object>> enrollKeyStore(@RequestBody EnrollKeyStoreRequest enrollKeyStoreRequest) throws Exception {
        return ApiResponse.<Map<String, Object>>builder().response(certificateV1Service.enrollKeyStore(enrollKeyStoreRequest)).build();
    }

    @PostMapping("/certificateRequest")
    public ApiResponse<Map<String, Object>> certificateRequest(@RequestBody CertificateRequest certificateRequest) throws Exception {
        return ApiResponse.<Map<String, Object>>builder().response(certificateV1Service.certificateRequest(certificateRequest)).build();
    }

}
