package com.example.ws_cert.dto.ejb.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateSearchRequestV2 {
    private Pagination pagination;
    private List<SearchCriteriaV2> criteria;
    private Sort sort;
}
