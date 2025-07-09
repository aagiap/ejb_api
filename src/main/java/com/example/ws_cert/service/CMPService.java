package com.example.ws_cert.service;

import com.example.ws_cert.constant.ApiSuccessCode;
import com.example.ws_cert.constant.ErrorCode;
import com.example.ws_cert.dto.ejb.request.CmpIrRequest;
import com.example.ws_cert.dto.ejb.request.CmpRevocationRequest;
import com.example.ws_cert.dto.ejb.response.ApiErrorResponse;
import com.example.ws_cert.dto.ejb.response.CmpRevocationResponse;
import com.example.ws_cert.dto.ejb.response.EnrollCertificateResponse;
import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.utils.CmpUtils;
import com.example.ws_cert.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class CMPService {
    @Value("${ejbca.cmp.url}")
    private String cmpUrl;

    private final HttpUtils httpUtils;
    private final CmpUtils cmpUtils;

    private final ApiSuccessCode apiSuccessCode = ApiSuccessCode.SUCCESS;
    private final ErrorCode errorCode = ErrorCode.CMP_ERROR;


    public ApiResponse<?> sendInitializationRequest(String alias, CmpIrRequest request) {
        String url = cmpUrl + "/" + alias;
        byte[] requestBytes = cmpUtils.buildIrMessage(request);
        byte[] responseBytes = httpUtils.sendCmpRequest(url, "POST", requestBytes);
        Boolean isError = cmpUtils.isExceptionCmpOccurred(responseBytes);
        if (isError) {
            return ApiResponse.<ApiErrorResponse>builder()
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage())
                    .response(cmpUtils.cmpApiErrorResponse(responseBytes))
                    .build();
        }
        EnrollCertificateResponse certificate = cmpUtils.parseIrResponse(responseBytes);
        return ApiResponse.<EnrollCertificateResponse>builder()
                .code(apiSuccessCode.getCode())
                .message(apiSuccessCode.getMessage())
                .response(certificate)
                .build();
    }

    public ApiResponse<?> sendRevocationnRequest(String alias, CmpRevocationRequest request) {
        String url = cmpUrl + "/" + alias;
        byte[] requestBytes = cmpUtils.buildRrMessage(request);
        byte[] responseBytes = httpUtils.sendCmpRequest(url, "POST", requestBytes);
        if (cmpUtils.isExceptionCmpOccurred(responseBytes)) {
            return ApiResponse.<ApiErrorResponse>builder()
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage())
                    .response(cmpUtils.cmpApiErrorResponse(responseBytes))
                    .build();
        }
        CmpRevocationResponse parsedResponse = cmpUtils.parseRrResponse(responseBytes);

        return ApiResponse.<CmpRevocationResponse>builder()
                .code(apiSuccessCode.getCode())
                .message(apiSuccessCode.getMessage())
                .response(parsedResponse)
                .build();

    }
}
