package com.example.ws_cert.utils;

import com.example.ws_cert.constant.ErrorCode;
import com.example.ws_cert.dto.ejb.request.CmpIrRequest;
import com.example.ws_cert.dto.ejb.request.CmpRevocationRequest;
import com.example.ws_cert.dto.ejb.response.ApiErrorResponse;
import com.example.ws_cert.dto.ejb.response.CmpIrResponse;
import com.example.ws_cert.dto.ejb.response.CmpRevocationResponse;
import com.example.ws_cert.dto.ejb.response.EnrollCertificateResponse;
import com.example.ws_cert.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.cmp.*;
import org.bouncycastle.asn1.ocsp.RevokedInfo;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CmpUtils {
    private final CmpRequestBuilderUntils cmpRequestBuilderUntils;

    public byte[] buildIrMessage(CmpIrRequest cmpIrRequest){
        return cmpRequestBuilderUntils.buildIrMessage(cmpIrRequest);
    }

    public byte[] buildRrMessage(CmpRevocationRequest cmpRevocationRequest){
        return cmpRequestBuilderUntils.buildRevocationRequestMessage(cmpRevocationRequest);
    }




    public Boolean isExceptionCmpOccurred(byte[] responseBytes) {
        try {
            PKIMessage responseMessage = PKIMessage.getInstance(responseBytes);
            PKIBody body = responseMessage.getBody();
            int bodyType = body.getType();

            if (bodyType == PKIBody.TYPE_ERROR) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return true; // Nếu có lỗi trong parsing, coi như có exception
        }
    }

    public Boolean isRevocation(byte[] responseBytes) {
        try {
            PKIMessage responseMessage = PKIMessage.getInstance(responseBytes);
            PKIBody body = responseMessage.getBody();
            int bodyType = body.getType();

            if (bodyType == PKIBody.TYPE_REVOCATION_REP) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return true; // Nếu có lỗi trong parsing, coi như có exception
        }
    }

    public ApiErrorResponse cmpApiErrorResponse(byte[] responseBytes) {
        PKIMessage responseMessage = PKIMessage.getInstance(responseBytes);
        PKIBody body = responseMessage.getBody();

            ErrorMsgContent error = ErrorMsgContent.getInstance(body.getContent());
            PKIStatusInfo statusInfo = error.getPKIStatusInfo();

            int status = statusInfo.getStatus().intValueExact(); // 2 = rejection
            String statusString = statusInfo.getStatusString() != null
                    ? statusInfo.getStatusString().getStringAt(0).getString()
                    : "No status string";

        return ApiErrorResponse.builder()
                .code(status)
                .message(statusString)
                .build();
    }

    public EnrollCertificateResponse parseIrResponse(byte[] responseBytes) {
        try {
            PKIMessage responseMessage = PKIMessage.getInstance(responseBytes);
            PKIBody body = responseMessage.getBody();

            CertRepMessage rep = CertRepMessage.getInstance(body.getContent());

            PKIStatusInfo statusInfo = rep.getResponse()[0].getStatus();

            int status = statusInfo.getStatus().intValueExact(); // 0 = accepted, 2 = rejected

            CMPCertificate cmpCert = rep.getResponse()[0].getCertifiedKeyPair()
                    .getCertOrEncCert().getCertificate();
            byte[] certEncoded = cmpCert.getX509v3PKCert().getEncoded();

            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(certEncoded));
            return EnrollCertificateResponse.builder()
                    .code(status)
                    .subject_dn(certificate.getSubjectX500Principal().getName())
                    .certificate_base64(new CmpIrResponse(certificate).toPem())
                    .build();

        } catch (Exception e) {
            throw new AppException(ErrorCode.FAILED_TO_PARSE_RESPONSE);
        }
    }

    public CmpRevocationResponse parseRrResponse(byte[] responseBytes) {
        try {
            PKIMessage pkiMessage = PKIMessage.getInstance(responseBytes);
            PKIBody pkiBody = pkiMessage.getBody();

            if (pkiBody.getType() != PKIBody.TYPE_REVOCATION_REP) {
                return CmpRevocationResponse.builder()
                        .code(ErrorCode.FAILED_TO_PARSE_RESPONSE.getCode())
                        .message("Unexpected CMP body type for revocation response: " + pkiBody.getType())
                        .build();
            }

            RevRepContent revRepContent = (RevRepContent) pkiBody.getContent();
            // Sửa lỗi: revRepContent.getStatus() trả về mảng PKIStatusInfo[]
            PKIStatusInfo pkiStatusInfo = revRepContent.getStatus()[0]; // <-- SỬA ĐỔI Ở ĐÂY

            int status = pkiStatusInfo.getStatus().intValue();
            String statusString = pkiStatusInfo.getStatusString() != null ?
                    pkiStatusInfo.getStatusString().getStringAt(0).getString() : "No status message provided by EJBCA.";

                return CmpRevocationResponse.builder()
                        .code(status)
                        .message(statusString)
                        .build();

        } catch (Exception e) {
            throw new AppException(ErrorCode.FAILED_TO_PARSE_RESPONSE);
        }
    }



}
