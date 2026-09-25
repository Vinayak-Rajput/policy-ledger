package com.hdfclife.ledger.service;

import com.hdfclife.ledger.domain.Customer;
import com.hdfclife.ledger.domain.Policy;
import com.hdfclife.ledger.domain.Rider;
import com.hdfclife.ledger.dto.CreatePolicyRequest;
import com.hdfclife.ledger.dto.PolicyResponse;
import com.hdfclife.ledger.exception.DuplicatePolicyException;
import com.hdfclife.ledger.exception.InvalidRequestException;
import com.hdfclife.ledger.exception.PolicyNotFoundException;
import com.hdfclife.ledger.repo.CustomerRepository;
import com.hdfclife.ledger.repo.PolicyRepository;
import com.hdfclife.ledger.repo.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;
    private final RiderRepository riderRepository;

    @Transactional(readOnly = true)
    public List<PolicyResponse> getAllPolicies() {

        return policyRepository.findAllByOrderByPolicyNoAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PolicyResponse getPolicyByNo(String policyNo) {

        Policy policy = policyRepository.findByPolicyNo(policyNo)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + policyNo));

        return mapToResponse(policy);
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> getPoliciesByStatus(String status) {

        return policyRepository.findByStatusOrderByPolicyNoAsc(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> getPoliciesByType(String productType) {

        return policyRepository.findByProductTypeOrderByPolicyNoAsc(productType).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> getPoliciesByCustomer(String customerName) {

        return policyRepository.findByCustomer_FullNameOrderByPolicyNoAsc(customerName).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> searchByMinPremium(Integer minPremium) {

        if (minPremium == null || minPremium < 0) {

            throw new InvalidRequestException("minPremium must be greater than or equal to 0");
        }

        return policyRepository.findWithPremiumAtLeast(minPremium).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PolicyResponse createPolicy(CreatePolicyRequest request) {

        if (policyRepository.existsByPolicyNo(request.getPolicyNo())) {

            throw new DuplicatePolicyException("Policy already exists: " + request.getPolicyNo());
        }

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseGet(() -> customerRepository.save(new Customer(request.getCustomer(), request.getEmail())));

        Policy policy = new Policy(
                request.getPolicyNo(),
                customer,
                request.getType(),
                request.getBasePremium(),
                request.getStatus()
        );

        Policy savedPolicy = policyRepository.save(policy);

        return mapToResponse(savedPolicy);
    }

    @Transactional
    public void deletePolicy(String policyNo) {

        Policy policy = policyRepository.findByPolicyNo(policyNo)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + policyNo));

        policyRepository.delete(policy);
    }

    private PolicyResponse mapToResponse(Policy policy) {

        List<String> riderCodes = policy.getRiders().stream()
                .map(Rider::getCode)
                .sorted()
                .collect(Collectors.toList());

        return new PolicyResponse(
                policy.getPolicyNo(),
                policy.getCustomer().getFullName(),
                policy.getCustomer().getEmail(),
                policy.getProductType(),
                policy.getBasePremium(),
                policy.getStatus(),
                riderCodes
        );
    }
}