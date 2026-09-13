package com.bloodbank.dto;

import com.bloodbank.model.Donation;

public class DonationResponseDTO {
    private Long id;
    private String donationDate;
    private String donationType;
    private Boolean preScreeningPassed;
    private String deferralReason;

    public static DonationResponseDTO fromEntity(Donation donation) {
        DonationResponseDTO dto = new DonationResponseDTO();
        dto.id = donation.getId();
        dto.donationDate = donation.getDonationDate() != null ? donation.getDonationDate().toString() : null;
        dto.donationType = donation.getDonationType() != null ? donation.getDonationType().name() : null;
        dto.preScreeningPassed = donation.getPreScreeningPassed();
        dto.deferralReason = donation.getDeferralReason();
        return dto;
    }

    public Long getId() { return id; }
    public String getDonationDate() { return donationDate; }
    public String getDonationType() { return donationType; }
    public Boolean getPreScreeningPassed() { return preScreeningPassed; }
    public String getDeferralReason() { return deferralReason; }
}