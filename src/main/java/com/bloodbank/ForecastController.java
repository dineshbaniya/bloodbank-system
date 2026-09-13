package com.bloodbank;

import com.bloodbank.service.ForecastService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/forecast")
public class ForecastController {

    private final ForecastService forecastService;

    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping("/donations")
    public Map getDonationForecast() {
        return forecastService.getForecastFromDatabase();
    }
}