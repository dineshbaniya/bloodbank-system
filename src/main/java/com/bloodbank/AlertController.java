package com.bloodbank;

import com.bloodbank.dto.AlertLogResponseDTO;
import com.bloodbank.dto.DonorResponseDTO;
import com.bloodbank.model.AlertLog;
import com.bloodbank.model.BloodRequest;
import com.bloodbank.model.Donor;
import com.bloodbank.repository.BloodRequestRepository;
import com.bloodbank.service.AlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;
    private final BloodRequestRepository bloodRequestRepository;

    public AlertController(AlertService alertService, BloodRequestRepository bloodRequestRepository) {
        this.alertService = alertService;
        this.bloodRequestRepository = bloodRequestRepository;
    }

    @GetMapping("/search")
    public List<DonorResponseDTO> searchNearbyDonors(
            @RequestParam String bloodGroup,
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radiusKm) {
        List<Donor> donors = alertService.findNearbyDonors(bloodGroup, lat, lng, radiusKm);
        return donors.stream().map(DonorResponseDTO::fromEntity).toList();
    }

    @PostMapping("/send/{requestId}")
    public List<AlertLogResponseDTO> sendInitialAlerts(
            @PathVariable Long requestId,
            @RequestParam double lat,
            @RequestParam double lng) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));
        List<AlertLog> logs = alertService.sendInitialAlerts(request, lat, lng);
        return logs.stream().map(AlertLogResponseDTO::fromEntity).toList();
    }

    @PostMapping("/escalate/{requestId}")
    public List<AlertLogResponseDTO> escalate(
            @PathVariable Long requestId,
            @RequestParam double lat,
            @RequestParam double lng) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));
        List<AlertLog> logs = alertService.escalateIfNoResponse(request, lat, lng);
        return logs.stream().map(AlertLogResponseDTO::fromEntity).toList();
    }
}