package com.bloodbank.service;

import com.bloodbank.model.FulfillmentMethod;
import com.bloodbank.model.BloodInventory;
import com.bloodbank.model.BloodRequest;
import com.bloodbank.model.InventoryStatus;
import com.bloodbank.model.RequestStatus;
import com.bloodbank.repository.BloodInventoryRepository;
import com.bloodbank.repository.BloodRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BloodInventoryService {

    private final BloodInventoryRepository bloodInventoryRepository;
    private final BloodRequestRepository bloodRequestRepository;

    public BloodInventoryService(BloodInventoryRepository bloodInventoryRepository,
                                  BloodRequestRepository bloodRequestRepository) {
        this.bloodInventoryRepository = bloodInventoryRepository;
        this.bloodRequestRepository = bloodRequestRepository;
    }

    @Transactional
    public List<BloodInventory> reserveUnits(BloodRequest request, int quantity) {

        List<BloodInventory> candidates = bloodInventoryRepository.findAvailableUnitsForUpdate(
                request.getBloodGroup(),
                request.getComponentType()
        );

        List<BloodInventory> reserved = new ArrayList<>();

        for (BloodInventory unit : candidates) {
            if (reserved.size() >= quantity) break;

            unit.setStatus(InventoryStatus.RESERVED);
            unit.setReservedForRequest(request);
            unit.setReservedAt(LocalDateTime.now());
            reserved.add(bloodInventoryRepository.save(unit));
        }

        if (!reserved.isEmpty()) {
            int newFulfilled = request.getQuantityFulfilled() + reserved.size();
            request.setQuantityFulfilled(newFulfilled);

            if (request.getFulfillmentMethod() == null) {
                request.setFulfillmentMethod(FulfillmentMethod.BANK_STOCK);
            } else if (request.getFulfillmentMethod() == FulfillmentMethod.DONOR_ALERT) {
                request.setFulfillmentMethod(FulfillmentMethod.MIXED);
            }

            if (newFulfilled >= request.getQuantityNeeded()) {
                request.setStatus(RequestStatus.FULFILLED);
                request.setFulfilledAt(LocalDateTime.now());
            } else {
                request.setStatus(RequestStatus.PARTIALLY_FULFILLED);
            }

            bloodRequestRepository.save(request);
        }

        return reserved;
    }

    @Transactional
    public void issueUnits(BloodRequest request) {
        List<BloodInventory> reservedUnits = bloodInventoryRepository.findAll().stream()
                .filter(unit -> unit.getReservedForRequest() != null
                        && unit.getReservedForRequest().getId().equals(request.getId())
                        && unit.getStatus() == InventoryStatus.RESERVED)
                .toList();

        for (BloodInventory unit : reservedUnits) {
            unit.setStatus(InventoryStatus.ISSUED);
            unit.setIssuedToOrg(request.getRequestingOrg());
            bloodInventoryRepository.save(unit);
        }
    }

    @org.springframework.scheduling.annotation.Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void markExpiredUnits() {
        List<BloodInventory> expiredUnits = bloodInventoryRepository.findByStatusAndExpiryDateLessThan(
                InventoryStatus.AVAILABLE, LocalDate.now()
        );
        for (BloodInventory unit : expiredUnits) {
            unit.setStatus(InventoryStatus.EXPIRED);
            bloodInventoryRepository.save(unit);
        }
        if (!expiredUnits.isEmpty()) {
            System.out.println("Marked " + expiredUnits.size() + " unit(s) as EXPIRED.");
        }
    }
}