package com.bloodbank.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "donor_id")
    private Donor donor;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToOne
    @JoinColumn(name = "inventory_id")
    private BloodInventory inventory;

    private LocalDate donationDate;
    @Enumerated(EnumType.STRING)
    private DonationType donationType;
    private Boolean preScreeningPassed;
    private String deferralReason;

    @ManyToOne
    @JoinColumn(name = "recorded_by_user_id")
    private User recordedByUser;

    // --- Getters and setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Donor getDonor() { return donor; }
    public void setDonor(Donor donor) { this.donor = donor; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }

    public BloodInventory getInventory() { return inventory; }
    public void setInventory(BloodInventory inventory) { this.inventory = inventory; }

    public LocalDate getDonationDate() { return donationDate; }
    public void setDonationDate(LocalDate donationDate) { this.donationDate = donationDate; }

    public DonationType getDonationType() { return donationType; }
    public void setDonationType(DonationType donationType) { this.donationType = donationType; }

    public Boolean getPreScreeningPassed() { return preScreeningPassed; }
    public void setPreScreeningPassed(Boolean preScreeningPassed) { this.preScreeningPassed = preScreeningPassed; }

    public String getDeferralReason() { return deferralReason; }
    public void setDeferralReason(String deferralReason) { this.deferralReason = deferralReason; }

    public User getRecordedByUser() { return recordedByUser; }
    public void setRecordedByUser(User recordedByUser) { this.recordedByUser = recordedByUser; }
}