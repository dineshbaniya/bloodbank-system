package com.bloodbank.dto;

import com.bloodbank.model.AlertLog;
import java.time.LocalDateTime;

public class AlertLogResponseDTO {

    private Long id;
    private Long requestId;
    private DonorResponseDTO donor;
    private Double searchRadiusKm;
    private LocalDateTime sentAt;
    private String channel;
    private String status;

    public static AlertLogResponseDTO fromEntity(AlertLog log) {
        AlertLogResponseDTO dto = new AlertLogResponseDTO();
        dto.id = log.getId();
        dto.requestId = log.getRequest() != null ? log.getRequest().getId() : null;
        dto.donor = log.getDonor() != null ? DonorResponseDTO.fromEntity(log.getDonor()) : null;
        dto.searchRadiusKm = log.getSearchRadiusKm();
        dto.sentAt = log.getSentAt();
        dto.channel = log.getChannel() != null ? log.getChannel().name() : null;
        dto.status = log.getStatus() != null ? log.getStatus().name() : null;
        return dto;
    }

    public Long getId() { return id; }
    public Long getRequestId() { return requestId; }
    public DonorResponseDTO getDonor() { return donor; }
    public Double getSearchRadiusKm() { return searchRadiusKm; }
    public LocalDateTime getSentAt() { return sentAt; }
    public String getChannel() { return channel; }
    public String getStatus() { return status; }
}