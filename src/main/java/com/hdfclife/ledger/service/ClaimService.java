package com.hdfclife.ledger.service;

import com.hdfclife.ledger.domain.Claim;
import com.hdfclife.ledger.domain.Policy;
import com.hdfclife.ledger.dto.ClaimResponse;
import com.hdfclife.ledger.dto.CreateClaimRequest;
import com.hdfclife.ledger.exception.PolicyNotFoundException;
import com.hdfclife.ledger.repo.ClaimRepository;
import com.hdfclife.ledger.repo.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;

    @Transactional(readOnly = true)
    public List<ClaimResponse> getClaimsByPolicyNo(String policyNo) {

        if (!policyRepository.existsByPolicyNo(policyNo)) {

            throw new PolicyNotFoundException("Policy not found: " + policyNo);
        }

        return claimRepository.findByPolicy_PolicyNoOrderByClaimNoAsc(policyNo).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClaimResponse createClaim(CreateClaimRequest request) {

        Policy policy = policyRepository.findByPolicyNo(request.getPolicyNo())
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + request.getPolicyNo()));

        long count = claimRepository.count();

        String claimNo = String.format("CLM-%02d", count + 1);

        Claim claim = new Claim(
                claimNo,
                policy,
                request.getClaimAmount(),
                request.getUrgency(),
                "SUBMITTED"
        );

        Claim savedClaim = claimRepository.save(claim);

        return mapToResponse(savedClaim);
    }

    private ClaimResponse mapToResponse(Claim claim) {

        return new ClaimResponse(
                claim.getClaimNo(),
                claim.getPolicy().getPolicyNo(),
                claim.getAmount(),
                claim.getUrgency(),
                claim.getStatus()
        );
    }
}