package com.hdfclife.ledger.repo;

import com.hdfclife.ledger.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Policy findByPolicyNo(Long policyNo);

    ResponseEntity<List<Policy>> findByNameContainingIgnoreCase(String keyword);
}
