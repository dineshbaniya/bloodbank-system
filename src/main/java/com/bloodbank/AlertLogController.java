package com.bloodbank;

import com.bloodbank.model.AlertLog;
import com.bloodbank.model.Donor;
import com.bloodbank.model.User;
import com.bloodbank.repository.AlertLogRepository;
import com.bloodbank.repository.DonorRepository;
import com.bloodbank.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertlogs")
public class AlertLogController {

    private final AlertLogRepository alertLogRepository;
    private final DonorRepository donorRepository;
    private final UserRepository userRepository;

    public AlertLogController(AlertLogRepository alertLogRepository, DonorRepository donorRepository, UserRepository userRepository) {
        this.alertLogRepository = alertLogRepository;
        this.donorRepository = donorRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public AlertLog create(@RequestBody AlertLog alert) {
        return alertLogRepository.save(alert);
    }

    @GetMapping
    public List<AlertLog> getAll() {
        return alertLogRepository.findAll();
    }

    @GetMapping("/me")
    public List<com.bloodbank.dto.AlertLogResponseDTO> getMyAlerts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Donor donor = donorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("No donor profile linked to this account"));

        return alertLogRepository.findByDonorId(donor.getId())
                .stream()
                .map(com.bloodbank.dto.AlertLogResponseDTO::fromEntity)
                .toList();
    }
}