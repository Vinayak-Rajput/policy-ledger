package com.hdfclife.ledger.web;

import com.hdfclife.ledger.domain.Claim;
import com.hdfclife.ledger.domain.Policy;

import com.hdfclife.ledger.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService policyService;
    @PostMapping
    public ResponseEntity<Policy> createPolicy(@RequestBody Policy policy) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(policyService.createPolicy(policy));
    }
    @GetMapping
    public ResponseEntity<List<Policy>> getAllPolicies(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String customerName
    ) {
        if(status != null && type != null && customerName != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(policyService.getPolicies(status,type,customerName));
        }

        if(status != null && type != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(policyService.getPoliciesByStatusType(status,type));
        }

        if(type != null && customerName != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(policyService.getPoliciesByTypeCustomer(type,customerName));
        }

        if(status != null && customerName != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(policyService.getPoliciesByStatusCustomer(status, customerName));
        }

        if(status != null){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(policyService.getPoliciesByStatus(status));
        }

        if(type != null){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(policyService.getPoliciesByType(type));
        }

        if(customerName != null){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(policyService.getPoliciesByCustomer(customerName));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.getPolicies());
    }

    @GetMapping("/{policyNo}")
    public ResponseEntity<Policy> getPolicyByNo(@PathVariable Long policyNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.getPolicyByNo(policyNo));
    }

    @GetMapping("/{policyNo}/claims")
    public ResponseEntity<List<Claim>> getClaimsByNo(@PathVariable Long policyNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.getClaimsByNo(policyNo));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Policy>> search(
            @RequestParam(value = "keyword", required = false) String keyword
    ){
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(policyService.getPolicies());
        }
        return policyService.search(keyword);

    }

    @DeleteMapping("/{policyNo}")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long policyNo) {
        return policyService.deletePolicy(policyNo);
    }

}
