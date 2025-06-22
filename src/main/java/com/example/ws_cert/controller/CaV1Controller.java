package com.example.ws_cert.controller;

import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.service.CaV1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/ca-v1")
@RequiredArgsConstructor
public class CaV1Controller {
    private final CaV1Service caV1Service;

    //Returns status, API version and EJBCA version.
    @GetMapping("/status")
    public ApiResponse<Map<String, Object>> getCaStatus() {
        return caV1Service.getCaStatus();
    }

    // Get PEM file with the active CA certificate chain.
    // không tải được cert của ManagementCA
    @GetMapping("/cert-download/{subjectDn}")
    public ApiResponse<Map<String, Object>> ertDownload(@PathVariable String subjectDn) {
        return caV1Service.certDownload(subjectDn);
    }

    // Return the latest CRL issued by this CA.
    @GetMapping("/get-latest-crl/{issuer_dn}")
    public ApiResponse<Map<String, Object>> getLastestCrl(@PathVariable String issuer_dn,
                                                          @RequestParam(required = false) boolean deltaCrl,
                                                          @RequestParam(defaultValue = "0") Integer crlPartitionIndex) {
        return caV1Service.getLastestCrl(issuer_dn, deltaCrl, crlPartitionIndex);
    }

    // Returns the Response containing the list of CAs with general information per CA as Json
    @GetMapping("/get-list-ca")
    public ApiResponse<Map<String, Object>> getLastestCrl(@RequestParam(defaultValue = "false") boolean includeExternal) {
        return caV1Service.getListCa(includeExternal);
    }

    // Import a certificate revocation list (CRL) for a CA.
    @PostMapping("/{issuerDn}/import-crl")
    public ApiResponse<Map<String, Object>> importCRL(@PathVariable String issuerDn,
                                                      @RequestPart("crlFile") MultipartFile crlFile) throws Exception {
        return caV1Service.importCrl(issuerDn, crlFile);
    }

    // Create CRL(main partition and delta) issued by this CA.
    @PostMapping("/create-crl/{issuer_dn}")
    public ApiResponse<Map<String, Object>> createCrl(@PathVariable String issuer_dn,
                                                      @RequestParam(defaultValue = "false") boolean deltacrl) throws Exception {
        return caV1Service.createCrl(issuer_dn, deltacrl);
    }


}
