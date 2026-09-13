package com.bloodbank.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "blood_inventory")
public class BloodInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "donor_id")
    private Donor donor;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;
    @Enumerated(EnumType.STRING)
    private ComponentType componentType;
    private LocalDate collectionDate;
    private LocalDate expiryDate;
    @Enumerated(EnumType.STRING)
    private InventoryStatus status = InventoryStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "reserved_for_request_id")
    private BloodRequest reservedForRequest;

    private LocalDateTime reservedAt;

    @ManyToOne
    @JoinColumn(name = "issued_to_org_id")
    private Organization issuedToOrg;

    // --- Getters and setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }

    public Donor getDonor() { return donor; }
    public void setDonor(Donor donor) { this.donor = donor; }

    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }

    public ComponentType getComponentType() { return componentType; }
    public void setComponentType(ComponentType componentType) { this.componentType = componentType; }

    public LocalDate getCollectionDate() { return collectionDate; }
    public void setCollectionDate(LocalDate collectionDate) { this.collectionDate = collectionDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public InventoryStatus getStatus() { return status; }
    public void setStatus(InventoryStatus status) { this.status = status; }

    public BloodRequest getReservedForRequest() { return reservedForRequest; }
    public void setReservedForRequest(BloodRequest reservedForRequest) { this.reservedForRequest = reservedForRequest; }

    public LocalDateTime getReservedAt() { return reservedAt; }
    public void setReservedAt(LocalDateTime reservedAt) { this.reservedAt = reservedAt; }

    public Organization getIssuedToOrg() { return issuedToOrg; }
    public void setIssuedToOrg(Organization issuedToOrg) { this.issuedToOrg = issuedToOrg; }
}