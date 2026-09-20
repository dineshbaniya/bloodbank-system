package com.bloodbank.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "blood_requests")
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requesting_org_id")
    private Organization requestingOrg;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @Enumerated(EnumType.STRING)
    private ComponentType componentType;
    private Integer quantityNeeded;
    private Integer quantityFulfilled = 0;
    @Enumerated(EnumType.STRING)
    private RequestUrgency urgency;
    private String patientName;
    private Integer patientAge;
    private Boolean isReplacementDonation = false;

    @ManyToOne
    @JoinColumn(name = "replacement_donor_id")
    private Donor replacementDonor;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private FulfillmentMethod fulfillmentMethod;

    @ManyToOne
    @JoinColumn(name = "requested_by_user_id")
    private User requestedByUser;

    private LocalDateTime fulfilledAt;

    // --- Getters and setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Organization getRequestingOrg() { return requestingOrg; }
    public void setRequestingOrg(Organization requestingOrg) { this.requestingOrg = requestingOrg; }

    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }

    public ComponentType getComponentType() { return componentType; }
    public void setComponentType(ComponentType componentType) { this.componentType = componentType; }

    public Integer getQuantityNeeded() { return quantityNeeded; }
    public void setQuantityNeeded(Integer quantityNeeded) { this.quantityNeeded = quantityNeeded; }

    public Integer getQuantityFulfilled() { return quantityFulfilled; }
    public void setQuantityFulfilled(Integer quantityFulfilled) { this.quantityFulfilled = quantityFulfilled; }

    public RequestUrgency getUrgency() { return urgency; }
    public void setUrgency(RequestUrgency urgency) { this.urgency = urgency; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Integer getPatientAge() { return patientAge; }
    public void setPatientAge(Integer patientAge) { this.patientAge = patientAge; }

    public Boolean getIsReplacementDonation() { return isReplacementDonation; }
    public void setIsReplacementDonation(Boolean isReplacementDonation) { this.isReplacementDonation = isReplacementDonation; }

    public Donor getReplacementDonor() { return replacementDonor; }
    public void setReplacementDonor(Donor replacementDonor) { this.replacementDonor = replacementDonor; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public FulfillmentMethod getFulfillmentMethod() { return fulfillmentMethod; }
    public void setFulfillmentMethod(FulfillmentMethod fulfillmentMethod) { this.fulfillmentMethod = fulfillmentMethod; }

    public User getRequestedByUser() { return requestedByUser; }
    public void setRequestedByUser(User requestedByUser) { this.requestedByUser = requestedByUser; }

    public LocalDateTime getFulfilledAt() { return fulfilledAt; }
    public void setFulfilledAt(LocalDateTime fulfilledAt) { this.fulfilledAt = fulfilledAt; }
}