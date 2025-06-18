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


    @GetMapping("/status")
    public ApiResponse<Map<String, Object>> getCaStatus() throws Exception {
        return caV1Service.getCaStatus();
    }

    // không tải được cert của ManagementCA
    @GetMapping("/cert-download/{subjectDn}")
    public ApiResponse<Map<String, Object>> ertDownload(@PathVariable String subjectDn) throws Exception {
        return caV1Service.certDownload(subjectDn);
    }

    @GetMapping("/getLastestCrl/{issuer_dn}")
    public ApiResponse<Map<String, Object>> getLastestCrl(@PathVariable String issuer_dn,
                                                          @RequestParam(required = false) boolean deltaCrl,
                                                          @RequestParam(defaultValue = "0") Integer crlPartitionIndex) throws Exception {
        return caV1Service.getLastestCrl(issuer_dn, deltaCrl, crlPartitionIndex);
    }


    @GetMapping("/getListCa")
    public ApiResponse<Map<String, Object>> getLastestCrl(@RequestParam(defaultValue = "false") boolean includeExternal) throws Exception {
        return caV1Service.getListCa(includeExternal);
    }

    @PostMapping("/{issuerDn}/import-crl")
    public ApiResponse<Map<String, Object>> importCRL(@PathVariable String issuerDn,
                                                      @RequestPart("crlFile") MultipartFile crlFile) throws Exception {
        return caV1Service.importCrl(issuerDn, crlFile);
    }

    @PostMapping("/createCrl/{issuer_dn}")
    public ApiResponse<Map<String, Object>> createCrl(@PathVariable String issuer_dn,
                                                      @RequestParam(defaultValue = "false") boolean deltacrl) throws Exception {
        return caV1Service.createCrl(issuer_dn, deltacrl);
    }


}
