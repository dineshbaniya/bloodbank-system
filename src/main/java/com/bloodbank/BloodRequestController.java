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

    public BloodRequestController(BloodRequestRepository bloodRequestRepository, UserRepository userRepository) {
        this.bloodRequestRepository = bloodRequestRepository;
        this.userRepository = userRepository;
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
    public List<BloodRequest> getMyOrganizationRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOrganization() == null) {
            throw new RuntimeException("This account is not linked to any organization.");
        }

        return bloodRequestRepository.findByRequestingOrgId(user.getOrganization().getId());
    }
}