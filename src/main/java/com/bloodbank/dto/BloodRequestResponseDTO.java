package com.bloodbank.dto;

import com.bloodbank.model.BloodRequest;
import com.bloodbank.model.FulfillmentMethod;
import com.bloodbank.model.RequestStatus;

import java.time.LocalDateTime;

public class BloodRequestResponseDTO {
    private Long id;
    private String bloodGroup;
    private String componentType;
    private Integer quantityNeeded;
    private Integer quantityFulfilled;
    private String urgency;
    private String patientName;
    private RequestStatus status;
    private FulfillmentMethod fulfillmentMethod;
    private LocalDateTime fulfilledAt;
    private String friendlyStatus;

    public static BloodRequestResponseDTO fromEntity(BloodRequest request, boolean anyDonorResponded) {
        BloodRequestResponseDTO dto = new BloodRequestResponseDTO();
        dto.id = request.getId();
        dto.bloodGroup = request.getBloodGroup() != null ? request.getBloodGroup().name() : null;
        dto.componentType = request.getComponentType() != null ? request.getComponentType().name() : null;
        dto.quantityNeeded = request.getQuantityNeeded();
        dto.quantityFulfilled = request.getQuantityFulfilled();
        dto.urgency = request.getUrgency() != null ? request.getUrgency().name() : null;
        dto.patientName = request.getPatientName();
        dto.status = request.getStatus();
        dto.fulfillmentMethod = request.getFulfillmentMethod();
        dto.fulfilledAt = request.getFulfilledAt();
        dto.friendlyStatus = computeFriendlyStatus(request, anyDonorResponded);
        return dto;
    }

    private static String computeFriendlyStatus(BloodRequest request, boolean anyDonorResponded) {
        if (request.getStatus() == RequestStatus.FULFILLED) {
            if (request.getFulfillmentMethod() == FulfillmentMethod.BANK_STOCK) {
                return "Blood reserved and ready from our stock";
            } else if (request.getFulfillmentMethod() == FulfillmentMethod.MIXED) {
                return "Fulfilled using a mix of donor and bank stock";
            } else {
                return "A donor has provided blood — request fulfilled";
            }
        }
        if (anyDonorResponded) {
            return "A donor has responded and is on the way";
        }
        if (request.getFulfillmentMethod() == FulfillmentMethod.DONOR_ALERT) {
            return "Searching for a nearby donor — awaiting response";
        }
        if (request.getFulfillmentMethod() == FulfillmentMethod.BANK_STOCK
                || request.getStatus() == RequestStatus.PARTIALLY_FULFILLED) {
            return "Partially reserved from our stock — more units needed";
        }
        return "Request received — awaiting action from blood bank staff";
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getBloodGroup() { return bloodGroup; }
    public String getComponentType() { return componentType; }
    public Integer getQuantityNeeded() { return quantityNeeded; }
    public Integer getQuantityFulfilled() { return quantityFulfilled; }
    public String getUrgency() { return urgency; }
    public String getPatientName() { return patientName; }
    public RequestStatus getStatus() { return status; }
    public FulfillmentMethod getFulfillmentMethod() { return fulfillmentMethod; }
    public LocalDateTime getFulfilledAt() { return fulfilledAt; }
    public String getFriendlyStatus() { return friendlyStatus; }
}