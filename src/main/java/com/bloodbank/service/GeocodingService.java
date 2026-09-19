package com.bloodbank.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate;

    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

   public double[] geocodeAddress(String address) {
    if (address == null || address.isBlank()) {
        return null;
    }

    java.net.URI uri = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
            .queryParam("q", address)
            .queryParam("format", "json")
            .queryParam("limit", 1)
            .encode()
            .build()
            .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.set("User-Agent", "BloodBankSystem/1.0 (contact: admin@test.com)");
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    try {
        ResponseEntity<java.util.List> response = restTemplate.exchange(uri, HttpMethod.GET, entity, java.util.List.class);
        java.util.List results = response.getBody();
        if (results == null || results.isEmpty()) {
            System.out.println("Geocoding returned no results for: " + address);
            return null;
        }
        java.util.Map<String, Object> first = (java.util.Map<String, Object>) results.get(0);
        double lat = Double.parseDouble((String) first.get("lat"));
        double lon = Double.parseDouble((String) first.get("lon"));
        System.out.println("Geocoding succeeded for '" + address + "' -> lat=" + lat + ", lon=" + lon);
        return new double[]{lat, lon};
    } catch (Exception e) {
        System.out.println("Geocoding failed for address: " + address + " - " + e.getMessage());
        return null;
    }
}
}