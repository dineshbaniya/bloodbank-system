package com.bloodbank.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ForecastService {

    private final RestTemplate restTemplate;
    private final String pythonServiceUrl;

    public ForecastService(
            RestTemplate restTemplate,
            @Value("${AI_SERVICE_URL:http://127.0.0.1:5000}") String aiServiceUrl
    ) {
        this.restTemplate = restTemplate;
        this.pythonServiceUrl = aiServiceUrl + "/forecast/from-db";
    }

    public Map getForecastFromDatabase() {
        return restTemplate.getForObject(pythonServiceUrl, Map.class);
    }
}