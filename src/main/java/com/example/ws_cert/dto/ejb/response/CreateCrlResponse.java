package com.example.ws_cert.dto.ejb.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCrlResponse {
    private String issuer_dn;
    private int latest_crl_version;
    private int latest_delta_crl_version;
    private Map<String, Integer> latest_partition_crl_versions;
    private Map<String, Integer> latest_partition_delta_crl_versions;
    private boolean all_success;
}
