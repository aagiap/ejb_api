package com.example.ws_cert.controller;

import com.example.ws_cert.dto.ejb.response.CaListResponse;
import com.example.ws_cert.dto.ejb.response.CaStatusResponse;
import com.example.ws_cert.dto.ejb.response.CreateCrlResponse;
import com.example.ws_cert.dto.ejb.response.GetLastestcrlResponse;
import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.service.CaV1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/ca-v1")
@RequiredArgsConstructor
public class CaV1Controller {
    private final CaV1Service caV1Service;


//    @GetMapping("/status")
//    public ApiResponse<CaStatusResponse> getCaStatus() throws Exception {
//        return ApiResponse.<CaStatusResponse>builder().response(caV1Service.getCaStatus()).build();

    @GetMapping("/status")
    public ApiResponse<Map<String, Object>> getCaStatus() throws Exception {
        return ApiResponse.<Map<String, Object>>builder().response(caV1Service.getCaStatus()).build();
    }

    // không tải được cert của ManagementCA
    @GetMapping("/cert-download/{subjectDn}")
    public ApiResponse<String> certDownload(@PathVariable String subjectDn) throws Exception {
        return ApiResponse.<String>builder().response(caV1Service.certDownload(subjectDn)).build();
    }

    @GetMapping("/getLastestCrl/{issuer_dn}")
    public ApiResponse<GetLastestcrlResponse> getLastestCrl(@PathVariable String issuer_dn, @RequestParam boolean deltaCrl, @RequestParam Integer crlPartitionIndex) throws Exception {
        return ApiResponse.<GetLastestcrlResponse>builder().response(caV1Service.getLastestCrl(issuer_dn, deltaCrl, crlPartitionIndex)).build();
    }

//    @GetMapping("/getListCa")
//    public ApiResponse<CaListResponse> getLastestCrl(@RequestParam boolean includeExternal) throws Exception {
//        return ApiResponse.<CaListResponse>builder().response(caV1Service.getListCa(includeExternal)).build();
//    }

    @GetMapping("/getListCa")
    public ApiResponse<Map<String, Object>> getLastestCrl(@RequestParam boolean includeExternal) throws Exception {
        return ApiResponse.<Map<String, Object>>builder().response(caV1Service.getListCa(includeExternal)).build();
    }

    @PostMapping("/{issuerDn}/import-crl")
    public ApiResponse<Map<String, Object>> importCRL(@PathVariable String issuerDn, @RequestPart("crlFile") MultipartFile crlFile) throws Exception {
        return ApiResponse.<Map<String, Object>>builder().response(caV1Service.importCrl(issuerDn, crlFile)).build();
    }

    @PostMapping("/createCrl/{issuer_dn}")
    public ApiResponse<Map<String, Object>> createCrl(@PathVariable String issuer_dn, @RequestParam boolean deltacrl) throws Exception {
        return ApiResponse.<Map<String, Object>>builder().response(caV1Service.createCrl(issuer_dn, deltacrl)).build();
    }


}
