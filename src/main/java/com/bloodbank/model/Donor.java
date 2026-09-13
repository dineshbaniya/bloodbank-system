package com.bloodbank.model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "donors")
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nullable: walk-in donors have no login
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Which blood bank/camp registered this donor
    @ManyToOne
    @JoinColumn(name = "registered_by_org_id")
    private Organization registeredByOrg;

    private String fullName;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
     private Gender gender;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;
    private String address;
    private Double latitude;
    private Double longitude;
    private LocalDate lastDonationDate;
    private Integer totalDonations = 0;
    private Double responseRate = 0.0;
    private Boolean isActive = true;
    private String deferralNotes;

    // --- Getters and setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Organization getRegisteredByOrg() { return registeredByOrg; }
    public void setRegisteredByOrg(Organization registeredByOrg) { this.registeredByOrg = registeredByOrg; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public LocalDate getLastDonationDate() { return lastDonationDate; }
    public void setLastDonationDate(LocalDate lastDonationDate) { this.lastDonationDate = lastDonationDate; }

    public Integer getTotalDonations() { return totalDonations; }
    public void setTotalDonations(Integer totalDonations) { this.totalDonations = totalDonations; }

    public Double getResponseRate() { return responseRate; }
    public void setResponseRate(Double responseRate) { this.responseRate = responseRate; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getDeferralNotes() { return deferralNotes; }
    public void setDeferralNotes(String deferralNotes) { this.deferralNotes = deferralNotes; }
        @Transient
    public boolean isEligibleToDonate() {
        if (Boolean.FALSE.equals(isActive)) return false;

        boolean gapOk = lastDonationDate == null
                || java.time.temporal.ChronoUnit.DAYS.between(lastDonationDate, LocalDate.now()) >= 90;

        boolean ageOk = true;
        if (dateOfBirth != null) {
            int age = java.time.Period.between(dateOfBirth, LocalDate.now()).getYears();
            ageOk = age >= 18 && age <= 65;
        }
        return gapOk && ageOk;
    }
}