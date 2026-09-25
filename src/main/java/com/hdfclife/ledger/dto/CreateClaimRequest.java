package com.hdfclife.ledger.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateClaimRequest {

    @NotBlank
    @Pattern(regexp = "HDFC-LIFE-[0-9]{4}")
    private String policyNo;

    @NotNull
    @Min(1)
    @Max(500000)
    private Integer claimAmount;

    @NotBlank
    @Pattern(regexp = "HIGH|MEDIUM|LOW")
    private String urgency;

    @Size(max = 120)
    private String hospitalName;

}
