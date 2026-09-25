package com.hdfclife.ledger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClaimResponse {

    private String claimNo;

    private String policyNo;

    private Integer claimAmount;

    private String urgency;

    private String status;

}
