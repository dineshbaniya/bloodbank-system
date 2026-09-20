package com.bloodbank.repository;

import com.bloodbank.model.BloodInventory;
import com.bloodbank.model.BloodGroup;
import com.bloodbank.model.ComponentType;
import com.bloodbank.model.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.List;

public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("""
        SELECT b FROM BloodInventory b
        WHERE b.bloodGroup = :bloodGroup
          AND b.componentType = :componentType
          AND b.status = com.bloodbank.model.InventoryStatus.AVAILABLE
          AND b.expiryDate >= CURRENT_DATE
        ORDER BY b.expiryDate ASC
        """)
        List<BloodInventory> findAvailableUnitsForUpdate(
        @Param("bloodGroup") BloodGroup bloodGroup,
        @Param("componentType") ComponentType componentType
          );

    List<BloodInventory> findByStatusAndExpiryDateLessThanEqualOrderByExpiryDateAsc(
            InventoryStatus status, java.time.LocalDate cutoffDate
    );
    List<BloodInventory> findByStatusAndExpiryDateLessThan(InventoryStatus status, java.time.LocalDate date);
}