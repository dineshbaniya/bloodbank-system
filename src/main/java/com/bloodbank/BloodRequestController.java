package com.bloodbank;

import com.bloodbank.model.BloodRequest;
import com.bloodbank.model.User;
import com.bloodbank.repository.BloodRequestRepository;
import com.bloodbank.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class BloodRequestController {

private final BloodRequestRepository bloodRequestRepository;
private final UserRepository userRepository;
private final com.bloodbank.repository.AlertLogRepository alertLogRepository;

public BloodRequestController(BloodRequestRepository bloodRequestRepository, UserRepository userRepository,
                               com.bloodbank.repository.AlertLogRepository alertLogRepository) {
    this.bloodRequestRepository = bloodRequestRepository;
    this.userRepository = userRepository;
    this.alertLogRepository = alertLogRepository;
}

    @PostMapping
    public BloodRequest create(@RequestBody BloodRequest request) {
        return bloodRequestRepository.save(request);
    }

    @GetMapping
    public List<BloodRequest> getAll() {
        return bloodRequestRepository.findAll();
    }

   @GetMapping("/my-organization")
public List<com.bloodbank.dto.BloodRequestResponseDTO> getMyOrganizationRequests() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (user.getOrganization() == null) {
        throw new RuntimeException("This account is not linked to any organization.");
    }

    List<BloodRequest> requests = bloodRequestRepository.findByRequestingOrgId(user.getOrganization().getId());

    return requests.stream()
            .map(request -> {
                boolean anyResponded = alertLogRepository.existsByRequestIdAndStatus(
                        request.getId(), com.bloodbank.model.AlertStatus.RESPONDED
                );
                return com.bloodbank.dto.BloodRequestResponseDTO.fromEntity(request, anyResponded);
            })
            .toList();
}
}