package com.bloodbank.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ForecastService {

    private final RestTemplate restTemplate;

    private static final String PYTHON_SERVICE_URL = "http://127.0.0.1:5000/forecast/from-db";

    public ForecastService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map getForecastFromDatabase() {
        return restTemplate.getForObject(PYTHON_SERVICE_URL, Map.class);
    }
}