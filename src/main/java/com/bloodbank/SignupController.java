package com.bloodbank;

import com.bloodbank.dto.DonorSignupRequest;
import com.bloodbank.model.BloodGroup;
import com.bloodbank.model.Donor;
import com.bloodbank.model.User;
import com.bloodbank.model.UserRole;
import com.bloodbank.repository.DonorRepository;
import com.bloodbank.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signup")
public class SignupController {

    private final UserRepository userRepository;
private final DonorRepository donorRepository;
private final com.bloodbank.repository.OrganizationRepository organizationRepository;
private final PasswordEncoder passwordEncoder;
private final com.bloodbank.service.GeocodingService geocodingService;

public SignupController(UserRepository userRepository, DonorRepository donorRepository,
                         com.bloodbank.repository.OrganizationRepository organizationRepository,
                         PasswordEncoder passwordEncoder,
                         com.bloodbank.service.GeocodingService geocodingService) {
    this.userRepository = userRepository;
    this.donorRepository = donorRepository;
    this.organizationRepository = organizationRepository;
    this.passwordEncoder = passwordEncoder;
    this.geocodingService = geocodingService;
}

    @PostMapping("/donor")
    public String registerDonor(@RequestBody DonorSignupRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("An account with this email already exists.");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.DONOR);
        User savedUser = userRepository.save(user);

       Donor donor = new Donor();
       donor.setFullName(request.getFullName());
       donor.setPhoneNumber(request.getPhoneNumber());
       donor.setBloodGroup(BloodGroup.valueOf(request.getBloodGroup()));
          donor.setAddress(request.getAddress());
       donor.setUser(savedUser);

      double[] coordinates = geocodingService.geocodeAddress(request.getAddress());
     if (coordinates != null) {
       donor.setLatitude(coordinates[0]);
       donor.setLongitude(coordinates[1]);
     }

    donorRepository.save(donor);

        return "Account created successfully. You can now log in.";
    }
        @PostMapping("/hospital")
    public String registerHospital(@RequestBody com.bloodbank.dto.HospitalSignupRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("An account with this email already exists.");
        }

        com.bloodbank.model.Organization organization = new com.bloodbank.model.Organization();
        organization.setName(request.getHospitalName());
        organization.setAddress(request.getHospitalAddress());
        organization.setType(com.bloodbank.model.OrganizationType.HOSPITAL);
        com.bloodbank.model.Organization savedOrg = organizationRepository.save(organization);

        User user = new User();
        user.setFullName(request.getStaffFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.HOSPITAL_STAFF);
        user.setOrganization(savedOrg);
        userRepository.save(user);

        return "Hospital account created successfully. You can now log in.";
    }
}