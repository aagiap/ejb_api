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

    // Returns status, API version and EJBCA version.
    @GetMapping("/status")
    public ApiResponse<Map<String, Object>> getCaStatus() throws Exception {
        return certificateV1Service.getStatus();
    }

    // List of certificates expiring within specified number of days.
    @GetMapping("/expired")
    public ApiResponse<Map<String, Object>> getExpiredCert(@RequestParam(defaultValue = "false") Integer days,
                                                           @RequestParam(defaultValue = "false") Integer offset,
                                                           @RequestParam(defaultValue = "false") Integer maxNumberOfResults) throws Exception {
        return certificateV1Service.getExpiredCerts(days, offset, maxNumberOfResults);
    }

    // Checks revocation status of the specified certificate.
    @GetMapping("/revocation-status/{issuer_dn}/{certificate_serial_number}")
    public ApiResponse<Map<String, Object>> getRevocationStatus(@PathVariable String issuer_dn,
                                                                @PathVariable String certificate_serial_number) throws Exception {
        return certificateV1Service.checkRevocationStatus(issuer_dn, certificate_serial_number);
    }

    // Revokes the specified certificate, changes revocation reason for an already revoked certificate, sets invalidity or revocation date.
    @PutMapping("/revoke/{issuer_dn}/{certificate_serial_number}")
    public ApiResponse<Map<String, Object>> revoke(@PathVariable String issuer_dn,
                                                   @PathVariable String certificate_serial_number,
                                                   @RequestParam(required = false) String reason,
                                                   @RequestParam(required = false) String date,
                                                   @RequestParam(required = false) String invalidity_date) throws Exception {
        return certificateV1Service.revoke(issuer_dn, certificate_serial_number, reason, date, invalidity_date);
    }

    // Insert as many search criteria as needed.
    // A reference about allowed values for criteria could be found below, under SearchCertificateCriteriaRestRequest model.
    @PostMapping("/search")
    public ApiResponse<Map<String, Object>> searchCertificate(@RequestBody Map<String, Object> searchCertificateRequest) throws Exception {
        return certificateV1Service.searchCertificate(searchCertificateRequest);
    }

    // Enroll for a certificate given a PEM encoded PKCS#10 CSR.
    // Response Format is 'DER' (default when excluded) or 'PKCS7' in base64 encoded PEM format
    @PostMapping("/enroll")
    public ApiResponse<Map<String, Object>> enrollCertificate(@RequestBody EnrollRequest enrollRequest) throws Exception {
        return certificateV1Service.enrollCertificate(enrollRequest);
    }

    // Finalizes enrollment after administrator approval using request Id.
    @PostMapping("/finalize/{request_id}")
    public ApiResponse<Map<String, Object>> finalizeEnroll(@PathVariable String request_id,
                                                           @RequestBody FinalizeEnrollRequest finalizeEnrollRequest) throws Exception {
        return certificateV1Service.finalizeEnrollCertificate(request_id,finalizeEnrollRequest);
    }

    // Creates a keystore for the specified end entity.
    @PostMapping("/enroll-keystore")
    public ApiResponse<Map<String, Object>> enrollKeyStore(@RequestBody EnrollKeyStoreRequest enrollKeyStoreRequest) throws Exception {
        return certificateV1Service.enrollKeyStore(enrollKeyStoreRequest);
    }

    // Enroll for a certificate given a PEM encoded PKCS#10 CSR.
    @PostMapping("/certificate-request")
    public ApiResponse<Map<String, Object>> certificateRequest(@RequestBody CertificateRequest certificateRequest) throws Exception {
        return certificateV1Service.certificateRequest(certificateRequest);
    }

}
