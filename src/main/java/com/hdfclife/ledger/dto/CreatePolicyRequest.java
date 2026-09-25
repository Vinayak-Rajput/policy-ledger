package com.hdfclife.ledger.dto;

import com.hdfclife.ledger.validation.PolicyType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreatePolicyRequest {

    @NotBlank
    @Pattern(regexp = "HDFC-LIFE-[0-9]{4}", message = "Policy number must match pattern HDFC-LIFE-XXXX")
    private String policyNo;

    @NotBlank
    @Size(min = 2, max = 120)
    private String customer;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "TERM|ULIP|ENDOWMENT", message = "Type must be TERM, ULIP, or ENDOWMENT")
    @PolicyType
    private String type;

    @NotNull
    @Min(1)
    private Integer basePremium;

    @NotBlank
    @Pattern(regexp = "Active|Lapsed|Pending", message = "Status must be Active, Lapsed, or Pending")
    private String status;

}
