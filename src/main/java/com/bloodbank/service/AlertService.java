package com.bloodbank.service;

import com.bloodbank.model.*;
import com.bloodbank.repository.AlertLogRepository;
import com.bloodbank.repository.DonorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {

    private static final double[] RADIUS_TIERS_KM = {5, 10, 20, 40};

    private final DonorRepository donorRepository;
    private final AlertLogRepository alertLogRepository;
    private final NotificationService notificationService;

    public AlertService(DonorRepository donorRepository,
                         AlertLogRepository alertLogRepository,
                         NotificationService notificationService) {
        this.donorRepository = donorRepository;
        this.alertLogRepository = alertLogRepository;
        this.notificationService = notificationService;
    }

    public List<Donor> findNearbyDonors(String bloodGroup, double lat, double lng, double radiusKm) {
        return donorRepository.findEligibleDonorsWithinRadius(bloodGroup, lat, lng, radiusKm);
    }

    // Sends the FIRST round of alerts, at the smallest radius tier (5km)
    public List<AlertLog> sendInitialAlerts(BloodRequest request, double lat, double lng) {
        return sendAlertsAtRadius(request, lat, lng, RADIUS_TIERS_KM[0]);
    }

    // Call this again later (manually for now) to widen the search if nobody has responded
    public List<AlertLog> escalateIfNoResponse(BloodRequest request, double lat, double lng) {

        boolean alreadyResponded = alertLogRepository
                .existsByRequestIdAndStatus(request.getId(), AlertStatus.RESPONDED);

        if (alreadyResponded) {
            System.out.println("Someone already responded to request " + request.getId() + " - no need to widen.");
            return List.of();
        }

        List<AlertLog> existing = alertLogRepository.findByRequestId(request.getId());
        double lastRadius = existing.stream()
                .mapToDouble(AlertLog::getSearchRadiusKm)
                .max()
                .orElse(0);

        double nextRadius = nextTierAbove(lastRadius);
        if (nextRadius == -1) {
            System.out.println("Already tried the widest radius for request " + request.getId() + " - no more tiers left.");
            return List.of();
        }

        return sendAlertsAtRadius(request, lat, lng, nextRadius);
    }

    private List<AlertLog> sendAlertsAtRadius(BloodRequest request, double lat, double lng, double radiusKm) {
        List<Donor> donors = donorRepository.findEligibleDonorsWithinRadiusExcludingAlerted(
             request.getBloodGroup().name(), lat, lng, radiusKm, request.getId());

        return donors.stream().map(donor -> {
            String message = "Urgent: " + request.getBloodGroup().name() +
                    " blood needed nearby. Reply YES if you can donate today.";
            notificationService.sendSms(donor.getPhoneNumber(), message);

            AlertLog log = new AlertLog();
            log.setRequest(request);
            log.setDonor(donor);
            log.setSearchRadiusKm(radiusKm);
            log.setSentAt(LocalDateTime.now());
            log.setChannel(AlertChannel.SMS);
            log.setStatus(AlertStatus.SENT);
            return alertLogRepository.save(log);
        }).toList();
    }

    private double nextTierAbove(double currentRadius) {
        for (double tier : RADIUS_TIERS_KM) {
            if (tier > currentRadius) return tier;
        }
        return -1;
    }
}