package com.hdfclife.ledger.web;

import com.hdfclife.ledger.dto.ClaimResponse;
import com.hdfclife.ledger.dto.CreatePolicyRequest;
import com.hdfclife.ledger.dto.PolicyResponse;
import com.hdfclife.ledger.service.ClaimService;
import com.hdfclife.ledger.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
@Tag(
        name = "Policies",
        description = "Policy management endpoints"
)
public class PolicyController {

    private final PolicyService policyService;
    private final ClaimService claimService;

    @GetMapping
    @Operation(
            summary = "Get all policies or filter by status, type, or customer"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Policies retrieved successfully"
    )
    public ResponseEntity<List<PolicyResponse>> getPolicies(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String customer
    ) {
        if (status != null) {

            return ResponseEntity.ok(policyService.getPoliciesByStatus(status));

        } else if (type != null) {

            return ResponseEntity.ok(policyService.getPoliciesByType(type));

        } else if (customer != null) {

            return ResponseEntity.ok(policyService.getPoliciesByCustomer(customer));
        }
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    @GetMapping("/search")
    @Operation(summary = "Search policies with base premium at least minPremium")
    @ApiResponse(
            responseCode = "200",
            description = "Policies found"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid minPremium parameter"
    )
    public ResponseEntity<List<PolicyResponse>> searchPolicies(@RequestParam(required = false) Integer minPremium) {

        return ResponseEntity.ok(policyService.searchByMinPremium(minPremium));
    }

    @GetMapping("/{policyNo}")
    @Operation(summary = "Get policy by policy number")
    @ApiResponse(
            responseCode = "200",
            description = "Policy found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Policy not found"
    )
    public ResponseEntity<PolicyResponse> getPolicyByNo(@PathVariable String policyNo) {

        return ResponseEntity.ok(policyService.getPolicyByNo(policyNo));
    }

    @PostMapping
    @Operation(summary = "Create a new policy")
    @ApiResponse(
            responseCode = "201",
            description = "Policy created"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Validation error"
    )
    @ApiResponse(
            responseCode = "409",
            description = "Duplicate policy number")

    public ResponseEntity<PolicyResponse> createPolicy(@Valid @RequestBody CreatePolicyRequest request) {

        PolicyResponse created = policyService.createPolicy(request);

        return ResponseEntity.created(URI.create("/api/policies/" + created.getPolicyNo())).body(created);
    }

    @DeleteMapping("/{policyNo}")
    @Operation(summary = "Delete policy by policy number")
    @ApiResponse(
            responseCode = "204",
            description = "Policy deleted"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Policy not found"
    )
    public ResponseEntity<Void> deletePolicy(@PathVariable String policyNo) {

        policyService.deletePolicy(policyNo);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{policyNo}/claims")
    @Operation(summary = "Get claims for a policy")
    @ApiResponse(
            responseCode = "200",
            description = "Claims retrieved"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Policy not found"
    )
    public ResponseEntity<List<ClaimResponse>> getClaimsForPolicy(@PathVariable String policyNo) {

        return ResponseEntity.ok(claimService.getClaimsByPolicyNo(policyNo));
    }
}