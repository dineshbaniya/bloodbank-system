package com.bloodbank;

import com.bloodbank.model.BloodInventory;
import com.bloodbank.model.BloodRequest;
import com.bloodbank.repository.BloodRequestRepository;
import com.bloodbank.service.BloodInventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class BloodInventoryReservationController {

    private final BloodInventoryService bloodInventoryService;
    private final BloodRequestRepository bloodRequestRepository;

    public BloodInventoryReservationController(BloodInventoryService bloodInventoryService,
                                                 BloodRequestRepository bloodRequestRepository) {
        this.bloodInventoryService = bloodInventoryService;
        this.bloodRequestRepository = bloodRequestRepository;
    }

    @PostMapping("/reserve")
    public Map<String, Object> reserve(@RequestParam Long requestId, @RequestParam int quantity) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));

        List<BloodInventory> reserved = bloodInventoryService.reserveUnits(request, quantity);

        return Map.of(
                "requested", quantity,
                "reserved", reserved.size(),
                "fullySatisfied", reserved.size() >= quantity,
                "reservedUnitIds", reserved.stream().map(BloodInventory::getId).toList()
        );
    }
}