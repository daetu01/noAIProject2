package com.city.monitoring.observation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {
    private double temperature;

    private double maxTemp;

    private double minTemp;

    private String pcpMsg;

    private LocalDateTime referenceTime;

    private LocalDateTime sunrise;

    private LocalDateTime sunset;

    private String uvIndexLevel;

    private int uvIndex;

    private String uvMsg;

    private String pm25Index;

    private int pm25;

    private String pm10Index;

    private int pm10;
}
