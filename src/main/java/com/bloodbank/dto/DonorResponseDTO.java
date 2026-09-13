package com.bloodbank.dto;

import com.bloodbank.model.Donor;

public class DonorResponseDTO {

    private Long id;
    private String fullName;
    private String bloodGroup;
    private Integer totalDonations;
    private Double responseRate;
    private Boolean eligibleToDonate;

    // Notice there is NO phoneNumber field here at all - that's the entire point

    public static DonorResponseDTO fromEntity(Donor donor) {
        DonorResponseDTO dto = new DonorResponseDTO();
        dto.id = donor.getId();
        dto.fullName = donor.getFullName();
        dto.bloodGroup = donor.getBloodGroup() != null ? donor.getBloodGroup().name() : null;
        dto.totalDonations = donor.getTotalDonations();
        dto.responseRate = donor.getResponseRate();
        dto.eligibleToDonate = donor.isEligibleToDonate();
        return dto;
    }

    // --- Getters only - this object is read-only once built ---

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getBloodGroup() { return bloodGroup; }
    public Integer getTotalDonations() { return totalDonations; }
    public Double getResponseRate() { return responseRate; }
    public Boolean getEligibleToDonate() { return eligibleToDonate; }
}