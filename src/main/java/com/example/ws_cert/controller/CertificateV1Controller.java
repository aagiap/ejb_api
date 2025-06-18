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
        return certificateV1Service.getStatus();
    }

    @GetMapping("/expired")
    public ApiResponse<Map<String, Object>> getExpiredCert(@RequestParam(defaultValue = "false") Integer days,
                                                           @RequestParam(defaultValue = "false") Integer offset,
                                                           @RequestParam(defaultValue = "false") Integer maxNumberOfResults) throws Exception {
        return certificateV1Service.getExpiredCerts(days, offset, maxNumberOfResults);
    }

    @GetMapping("/revocationstatus/{issuer_dn}/{certificate_serial_number}")
    public ApiResponse<Map<String, Object>> getRevocationStatus(@PathVariable String issuer_dn,
                                                                @PathVariable String certificate_serial_number) throws Exception {
        return certificateV1Service.checkRevocationStatus(issuer_dn, certificate_serial_number);
    }

    @PutMapping("/revoke/{issuer_dn}/{certificate_serial_number}")
    public ApiResponse<Map<String, Object>> revoke(@PathVariable String issuer_dn,
                                                   @PathVariable String certificate_serial_number,
                                                   @RequestParam(required = false) String reason,
                                                   @RequestParam(required = false) String date,
                                                   @RequestParam(required = false) String invalidity_date) throws Exception {
        return certificateV1Service.revoke(issuer_dn, certificate_serial_number, reason, date, invalidity_date);
    }

    @PostMapping("/search")
    public ApiResponse<Map<String, Object>> searchCertificate(@RequestBody Map<String, Object> searchCertificateRequest) throws Exception {
        return certificateV1Service.searchCertificate(searchCertificateRequest);
    }

    @PostMapping("/enroll")
    public ApiResponse<Map<String, Object>> enrollCertificate(@RequestBody EnrollRequest enrollRequest) throws Exception {
        return certificateV1Service.enrollCertificate(enrollRequest);
    }

    @PostMapping("/finalize/{request_id}")
    public ApiResponse<Map<String, Object>> finalizeEnroll(@PathVariable String request_id,
                                                           @RequestBody FinalizeEnrollRequest finalizeEnrollRequest) throws Exception {
        return certificateV1Service.finalizeEnrollCertificate(request_id,finalizeEnrollRequest);
    }

    @PostMapping("/enrollKeyStore")
    public ApiResponse<Map<String, Object>> enrollKeyStore(@RequestBody EnrollKeyStoreRequest enrollKeyStoreRequest) throws Exception {
        return certificateV1Service.enrollKeyStore(enrollKeyStoreRequest);
    }

    @PostMapping("/certificateRequest")
    public ApiResponse<Map<String, Object>> certificateRequest(@RequestBody CertificateRequest certificateRequest) throws Exception {
        return certificateV1Service.certificateRequest(certificateRequest);
    }

}
