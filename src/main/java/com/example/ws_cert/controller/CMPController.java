package com.example.ws_cert.controller;

import com.example.ws_cert.dto.ejb.request.CmpIrRequest;
import com.example.ws_cert.dto.ejb.request.CmpRevocationRequest;
import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.service.CMPService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;


@RestController
@RequestMapping("/cmp")
@RequiredArgsConstructor
public class CMPController {
    private final CMPService cmpService;

    @PostMapping("/ir/{alias}")
    public ApiResponse<?> sendIrRequest(@RequestBody CmpIrRequest request, @PathVariable String alias) {
        return cmpService.sendInitializationRequest(alias, request);
    }

    @PostMapping("/rr/{alias}")
    public ApiResponse<?> sendRrRequest(@RequestBody CmpRevocationRequest request, @PathVariable String alias) {
        request.setSerialNumber(new BigInteger(request.getSerialNumberHex(), 16));
        return cmpService.sendRevocationnRequest(alias, request);
    }
}
