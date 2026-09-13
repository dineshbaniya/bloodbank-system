package com.bloodbank.repository;

import com.bloodbank.model.AlertLog;
import com.bloodbank.model.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertLogRepository extends JpaRepository<AlertLog, Long> {

    List<AlertLog> findByRequestId(Long requestId);

    List<AlertLog> findByDonorId(Long donorId);

    boolean existsByRequestIdAndStatus(Long requestId, AlertStatus status);
}