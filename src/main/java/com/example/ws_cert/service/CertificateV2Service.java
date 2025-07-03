package com.example.ws_cert.service;

import com.example.ws_cert.dto.ejb.request.CertificateSearchRequestV2;
import com.example.ws_cert.dto.response.ApiResponse;
import com.example.ws_cert.utils.HttpUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CertificateV2Service {
    private final HttpUtils httpUtils;

    @Value("${ejbca.url}")
    private String ejbcaUrl;

    @Value("${ejbca.url.prefix-v2-certificate}")
    private String prefixUrl;

    private String certV2Url;

    @PostConstruct
    public void init() {
        certV2Url = ejbcaUrl + prefixUrl;
    }


    public ApiResponse<Map<String, Object>> getStatus() {

        String url = certV2Url + "/status";
        HttpRequest request = httpUtils.build(url, "GET", null);
        return httpUtils.getStringObjectMap(request);
    }

    public ApiResponse<Map<String, Object>> markKeyRecovery(String issuer_dn, String certificate_serial_number) {
        String url = certV2Url + "/" + issuer_dn + "/" + certificate_serial_number + "/" + "keyrecovery";
        HttpRequest request = httpUtils.build(url, "PUT", null);
        return httpUtils.getStringObjectMap(request);
    }

    public ApiResponse<Map<String, Object>> getCertProfile(String profile_name) {
        String url = certV2Url + "/profile_name" + profile_name;
        HttpRequest request = httpUtils.build(url, "GET", null);
        return httpUtils.getStringObjectMap(request);
    }

    public ApiResponse<Map<String, Object>> countActiveCert(Boolean isActive) {
        String url = certV2Url;
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("isActive", String.valueOf(isActive));
        url = httpUtils.appendQueryParams(url, queryParams);
        HttpRequest request = httpUtils.build(url, "GET", null);
        return httpUtils.getStringObjectMap(request);
    }

    public ApiResponse<Map<String, Object>> searchCertificate(CertificateSearchRequestV2 certificateSearchRequestV2) {
        String url = certV2Url + "/search";
        HttpRequest request = httpUtils.build(url, "POST", certificateSearchRequestV2);
        return httpUtils.getStringObjectMap(request);
    }
}
