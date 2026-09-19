package com.bloodbank;

import com.bloodbank.dto.DonationResponseDTO;
import com.bloodbank.model.Donation;
import com.bloodbank.model.Donor;
import com.bloodbank.model.User;
import com.bloodbank.repository.DonationRepository;
import com.bloodbank.repository.DonorRepository;
import com.bloodbank.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;
    private final UserRepository userRepository;

    public DonationController(DonationRepository donationRepository, DonorRepository donorRepository, UserRepository userRepository) {
        this.donationRepository = donationRepository;
        this.donorRepository = donorRepository;
        this.userRepository = userRepository;
    }

   @PostMapping
public Donation create(@RequestBody Donation donation) {
    Donation saved = donationRepository.save(donation);

    Donor donor = donation.getDonor();
    if (donor != null) {
        Donor existingDonor = donorRepository.findById(donor.getId())
                .orElseThrow(() -> new RuntimeException("Donor not found: " + donor.getId()));

        existingDonor.setTotalDonations(
                (existingDonor.getTotalDonations() == null ? 0 : existingDonor.getTotalDonations()) + 1
        );
        existingDonor.setLastDonationDate(
                donation.getDonationDate() != null ? donation.getDonationDate() : java.time.LocalDate.now()
        );
        donorRepository.save(existingDonor);
    }

    return saved;
}

    @GetMapping
    public List<Donation> getAll() {
        return donationRepository.findAll();
    }

    @GetMapping("/me")
    public List<DonationResponseDTO> getMyDonations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Donor donor = donorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("No donor profile linked to this account"));

        return donationRepository.findByDonorId(donor.getId())
                .stream()
                .map(DonationResponseDTO::fromEntity)
                .toList();
    }
}