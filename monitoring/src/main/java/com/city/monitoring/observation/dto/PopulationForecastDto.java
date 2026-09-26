package com.city.monitoring.observation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PopulationForecastDto {
    @JsonProperty("FCST_TIME")
    private String fcstTime;

    @JsonProperty("FCST_CONGEST_LVL")
    private String fcstCongestLvl;

    @JsonProperty("FCST_PPLTN_MIN")
    private int fcstPPLTNMIN;

    @JsonProperty("FCST_PPLTN_MAX")
    private int fcstPPLTNMAX;
}
