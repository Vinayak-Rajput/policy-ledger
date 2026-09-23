package com.hdfclife.ledger.service;

import com.hdfclife.ledger.domain.Claim;
import com.hdfclife.ledger.domain.Policy;
import com.hdfclife.ledger.exception.PolicyNotFoundException;
import com.hdfclife.ledger.repo.ClaimRepository;
import com.hdfclife.ledger.repo.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;

    public List<Policy> getPolicies() {

        return policyRepository.findAll();
    }

    public List<Policy> getPoliciesByStatus(String status) {

        return policyRepository.findAll().stream()
                .filter(p -> p.getStatus().equals(status))
                .toList();
    }

    public List<Policy> getPoliciesByType(String type) {

        return policyRepository.findAll().stream()
                .filter(p -> p.getProductType().equals(type))
                .toList();
    }

    public List<Policy> getPoliciesByCustomer(String customerName) {

        return policyRepository.findAll().stream()
                .filter(p -> p.getCustomer().getFullName().equals(customerName))
                .toList();
    }

    public List<Policy> getPolicies(String status, String type, String customerName) {
        return policyRepository.findAll().stream()
                .filter(p -> p.getStatus().equals(status))
                .filter(p -> p.getProductType().equals(type))
                .filter(p -> p.getCustomer().getFullName().equals(customerName))
                .toList();
    }

    public Policy getPolicyByNo(Long policyNo) {

        return policyRepository.findByPolicyNo(policyNo);
    }


    public List<Policy> getPoliciesByStatusType(String status, String type) {

        return policyRepository.findAll().stream()
                .filter(p -> p.getStatus().equals(status))
                .filter(p -> p.getProductType().equals(type))
                .toList();
    }

    public List<Policy> getPoliciesByTypeCustomer(String type, String customerName) {

        return policyRepository.findAll().stream()
                .filter(p -> p.getProductType().equals(type))
                .filter(p -> p.getCustomer().getFullName().equals(customerName))
                .toList();
    }

    public List<Policy> getPoliciesByStatusCustomer(String status, String customerName) {

        return policyRepository.findAll().stream()
                .filter(p -> p.getStatus().equals(status))
                .filter(p -> p.getCustomer().getFullName().equals(customerName))
                .toList();
    }

    public List<Claim> getClaimsByNo(Long policyNo) {

        return claimRepository.findAll().stream()
                .filter(c -> c.getPolicy().getPolicyNo().equals(policyNo))
                .toList();
    }

    public ResponseEntity<List<Policy>> search(String keyword) {

        return policyRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Policy createPolicy(Policy policy) {

        return policyRepository.save(policy);
    }

    public ResponseEntity<Void> deletePolicy(Long policyNo) {

        return policyRepository.deleteAllByPolicyNo(policyNo);
    }
}
