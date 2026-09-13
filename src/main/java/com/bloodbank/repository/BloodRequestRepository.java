package com.bloodbank.repository;

import com.bloodbank.model.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    java.util.List<com.bloodbank.model.BloodRequest> findByRequestingOrgId(Long orgId);
}