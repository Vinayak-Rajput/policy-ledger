package com.hdfclife.ledger.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claim_no", nullable = false, unique = true)
    private String claimNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "urgency", nullable = false)
    private String urgency;

    @Column(name = "status", nullable = false)
    private String status;

    public Claim(String claimNo, Policy policy, Integer amount, String urgency, String status) {
        this.claimNo = claimNo;
        this.policy = policy;
        this.amount = amount;
        this.urgency = urgency;
        this.status = status;
    }
}
