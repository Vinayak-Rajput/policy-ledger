package com.hdfclife.ledger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class PolicyResponse {

    private String policyNo;

    private String customer;

    private String email;

    private String type;

    private Integer basePremium;

    private String status;

    private List<String> riderCodes;
}
