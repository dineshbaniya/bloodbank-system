package com.bloodbank;

import com.bloodbank.dto.DonorResponseDTO;
import com.bloodbank.model.Donor;
import com.bloodbank.model.User;
import com.bloodbank.repository.DonorRepository;
import com.bloodbank.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    private final DonorRepository donorRepository;
    private final UserRepository userRepository;

    public DonorController(DonorRepository donorRepository, UserRepository userRepository) {
        this.donorRepository = donorRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public DonorResponseDTO create(@RequestBody Donor donor) {
        Donor saved = donorRepository.save(donor);
        return DonorResponseDTO.fromEntity(saved);
    }

    @GetMapping
    public List<DonorResponseDTO> getAll() {
        return donorRepository.findAll()
                .stream()
                .map(DonorResponseDTO::fromEntity)
                .toList();
    }

    @GetMapping("/me")
    public DonorResponseDTO getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Donor donor = donorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("No donor profile linked to this account"));

        return DonorResponseDTO.fromEntity(donor);
    }
}