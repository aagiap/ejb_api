package com.example.ws_cert.service;

import com.example.ws_cert.dto.ejb.request.*;
import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.utils.EjbTLSConnectionUtils;
import com.example.ws_cert.utils.HttpUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CertificateV1Service {
    private final EjbTLSConnectionUtils ejbTLSConnectionUtils;
    private final HttpUtils httpUtils;

    @Value("${ejbca.url}")
    private String ejbcaUrl;

    @Value("${ejbca.url.prefix-v1-certificate}")
    private String prefixUrl;

    private String certV1Url;

    @PostConstruct
    public void init() {
        certV1Url = ejbcaUrl + prefixUrl;
    }

    public ApiResponse<Map<String, Object>> getStatus() throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();

        String url = certV1Url + "/status";
        HttpRequest request = httpUtils.build(url, "GET", null);
        return httpUtils.getStringObjectMap(sslContext, request);
    }

    public ApiResponse<Map<String, Object>> getExpiredCerts(Integer days, Integer offset, Integer maxNumberOfResults) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();

        String url = certV1Url + "/expire" + "?days=" + days + "&offset=" + offset + "&maxNumberOfResults=" + maxNumberOfResults;
        HttpRequest request = httpUtils.build(url, "GET", null);
        return httpUtils.getStringObjectMap(sslContext, request);
    }

    public ApiResponse<Map<String, Object>> checkRevocationStatus(String issuer_dn, String certificate_serial_number) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();

        String url = certV1Url + "/" + issuer_dn + "/" + certificate_serial_number + "/" + "revocationstatus";
        HttpRequest request = httpUtils.build(url, "GET", null);
        return httpUtils.getStringObjectMap(sslContext, request);
    }

//    public Map<String, Object> searchCertificate(SearchCertificateRequest searchCertificateRequest) throws Exception {
//        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();
//
//        String url = certV1Url + "/search" ;
//        HttpRequest request = httpUtils.build(url, "POST", searchCertificateRequest);
//        return httpUtils.getStringObjectMap(sslContext, request);
//    }

    public ApiResponse<Map<String, Object>> searchCertificate(Map<String, Object> searchCertificateRequest) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();

        String url = certV1Url + "/search" ;
        HttpRequest request = httpUtils.build(url, "POST", searchCertificateRequest);
        return httpUtils.getStringObjectMap(sslContext, request);
    }

    public ApiResponse<Map<String, Object>> revoke(String issuer_dn, String certificate_serial_number) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();

        String url = certV1Url + "/" + issuer_dn + "/" + certificate_serial_number + "/" + "revoke";
        HttpRequest request = httpUtils.build(url, "PUT", null);
        return httpUtils.getStringObjectMap(sslContext, request);
    }


    public ApiResponse<Map<String, Object>> enrollCertificate(EnrollRequest enrollRequest) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();

        String url = certV1Url + "/pkcs10enroll" ;
        HttpRequest request = httpUtils.build(url, "POST", enrollRequest);
        return httpUtils.getStringObjectMap(sslContext, request);
    }

    public ApiResponse<Map<String, Object>> finalizeEnrollCertificate(String request_id, FinalizeEnrollRequest finalizeEnrollRequest) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();

        String url = certV1Url + "/" + request_id + "/finalize" ;
        HttpRequest request = httpUtils.build(url, "POST", finalizeEnrollRequest);
        return httpUtils.getStringObjectMap(sslContext, request);
    }

    public ApiResponse<Map<String, Object>> enrollKeyStore(EnrollKeyStoreRequest enrollKeyStoreRequest) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();

        String url = certV1Url + "/enrollkeystore" ;
        HttpRequest request = httpUtils.build(url, "POST", enrollKeyStoreRequest);
        return httpUtils.getStringObjectMap(sslContext, request);
    }

    public ApiResponse<Map<String, Object>> certificateRequest(CertificateRequest certificateRequest) throws Exception {
        SSLContext sslContext = ejbTLSConnectionUtils.createSSLContext();

        String url = certV1Url + "/certificaterequest" ;
        HttpRequest request = httpUtils.build(url, "POST", certificateRequest);
        return httpUtils.getStringObjectMap(sslContext, request);
    }
}
