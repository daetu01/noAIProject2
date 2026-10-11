package com.city.monitoring.observation.dto.seoul124Api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WeatherForecastDto {
    @JsonProperty("FCST_DT")
    private String fcstDt;

    @JsonProperty("TEMP")
    private int temp;

    @JsonProperty("RAIN_CHANCE")
    private int rainChance;

    @JsonProperty("SKY_STTS")
    private String skyStts;
}
