package com.bloodbank.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendSms(String phoneNumber, String message) {
        // TODO: replace with a real SMS gateway later (e.g. Sparrow SMS for Nepal)
        System.out.println("[SMS to " + phoneNumber + "]: " + message);
    }
}