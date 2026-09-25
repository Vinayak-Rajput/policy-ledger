package com.hdfclife.ledger.repo;

import com.hdfclife.ledger.domain.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {

    Optional<Rider> findByCode(String code);
}
