package com.example.ws_cert.dto.ejb.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLastestCrlRequest {
    private String issuer_dn;
    private boolean deltaCrl;
    private Integer crlPartitionIndex;
}
