package com.example.ws_cert.dto.ejb.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCertificateRequest {
    private List<CertificateSearchCriteria> criteria;
    private Integer max_number_of_results;
}
