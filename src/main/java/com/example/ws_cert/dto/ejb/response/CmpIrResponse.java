package com.example.ws_cert.dto.ejb.response;

import com.example.ws_cert.constant.ErrorCode;
import com.example.ws_cert.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.cert.X509Certificate;


@RequiredArgsConstructor
public class CmpIrResponse {
    private final X509Certificate certificate;

    public String toPem() {
        try {
        return "-----BEGIN CERTIFICATE-----\n" +
                java.util.Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(certificate.getEncoded()) +
                "\n-----END CERTIFICATE-----";

        } catch (Exception e) {
            throw new AppException(ErrorCode.FAILED_TO_ENCODE_BYTES_CODE);
        }
    }
}
