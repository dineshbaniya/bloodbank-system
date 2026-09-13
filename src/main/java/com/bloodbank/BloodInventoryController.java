package com.bloodbank;

import com.bloodbank.model.BloodInventory;
import com.bloodbank.model.InventoryStatus;
import com.bloodbank.repository.BloodInventoryRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class BloodInventoryController {

    private final BloodInventoryRepository bloodInventoryRepository;

    public BloodInventoryController(BloodInventoryRepository bloodInventoryRepository) {
        this.bloodInventoryRepository = bloodInventoryRepository;
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
}