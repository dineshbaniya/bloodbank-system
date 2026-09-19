package com.bloodbank.service;

import com.bloodbank.model.BloodInventory;
import com.bloodbank.model.BloodRequest;
import com.bloodbank.model.InventoryStatus;
import com.bloodbank.model.RequestStatus;
import com.bloodbank.repository.BloodInventoryRepository;
import com.bloodbank.repository.BloodRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}