package com.bloodbank.repository;

import com.bloodbank.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonorRepository extends JpaRepository<Donor, Long> {
        java.util.Optional<Donor> findByUserId(Long userId);

    // Used for plain manual searches (no request context)
    @Query(value = """
            SELECT d.*,
                   (6371 * acos(
                        cos(radians(:lat)) * cos(radians(d.latitude)) *
                        cos(radians(d.longitude) - radians(:lng)) +
                        sin(radians(:lat)) * sin(radians(d.latitude))
                   )) AS distance_km
            FROM donors d
            WHERE d.blood_group = :bloodGroup
              AND d.is_active = true
              AND d.latitude IS NOT NULL
              AND d.longitude IS NOT NULL
            HAVING distance_km <= :radiusKm
            ORDER BY distance_km ASC
            """, nativeQuery = true)
    List<Donor> findEligibleDonorsWithinRadius(
            @Param("bloodGroup") String bloodGroup,
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radiusKm") double radiusKm
    );

    // Used for actual alerting - excludes donors already contacted for this exact request
    @Query(value = """
            SELECT d.*,
                   (6371 * acos(
                        cos(radians(:lat)) * cos(radians(d.latitude)) *
                        cos(radians(d.longitude) - radians(:lng)) +
                        sin(radians(:lat)) * sin(radians(d.latitude))
                   )) AS distance_km
            FROM donors d
            WHERE d.blood_group = :bloodGroup
              AND d.is_active = true
              AND d.latitude IS NOT NULL
              AND d.longitude IS NOT NULL
              AND d.id NOT IN (
                    SELECT donor_id FROM alert_logs WHERE request_id = :requestId
              )
            HAVING distance_km <= :radiusKm
            ORDER BY distance_km ASC
            """, nativeQuery = true)
    List<Donor> findEligibleDonorsWithinRadiusExcludingAlerted(
            @Param("bloodGroup") String bloodGroup,
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radiusKm") double radiusKm,
            @Param("requestId") Long requestId
    );
}