package com.bloodbank;

import com.bloodbank.model.BloodInventory;
import com.bloodbank.model.BloodRequest;
import com.bloodbank.model.InventoryStatus;
import com.bloodbank.repository.BloodInventoryRepository;
import com.bloodbank.repository.BloodRequestRepository;
import com.bloodbank.service.BloodInventoryService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class BloodInventoryController {

    private final BloodInventoryRepository bloodInventoryRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final BloodInventoryService bloodInventoryService;

    public BloodInventoryController(BloodInventoryRepository bloodInventoryRepository,
                                     BloodRequestRepository bloodRequestRepository,
                                     BloodInventoryService bloodInventoryService) {
        this.bloodInventoryRepository = bloodInventoryRepository;
        this.bloodRequestRepository = bloodRequestRepository;
        this.bloodInventoryService = bloodInventoryService;
    }

    @PostMapping
    public BloodInventory create(@RequestBody BloodInventory unit) {
        return bloodInventoryRepository.save(unit);
    }

    @GetMapping
    public List<BloodInventory> getAll() {
        return bloodInventoryRepository.findAll();
    }

    @GetMapping("/nearing-expiry")
    public List<BloodInventory> getNearingExpiry(@RequestParam(defaultValue = "5") int withinDays) {
        LocalDate cutoff = LocalDate.now().plusDays(withinDays);
        return bloodInventoryRepository.findByStatusAndExpiryDateLessThanEqualOrderByExpiryDateAsc(
                InventoryStatus.AVAILABLE, cutoff
        );
    }

    @PostMapping("/issue/{requestId}")
    public String issue(@PathVariable Long requestId) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));
        bloodInventoryService.issueUnits(request);
        String orgName = request.getRequestingOrg() != null ? request.getRequestingOrg().getName() : "the requesting hospital";
        return "Units issued to " + orgName;
    }
}