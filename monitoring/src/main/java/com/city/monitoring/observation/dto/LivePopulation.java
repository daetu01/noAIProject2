package com.city.monitoring.observation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class LivePopulation {
    @JsonProperty("AREA_PPLTN_MIN")
    private Integer populationMin;

    @JsonProperty("AREA_PPLTN_MAX")
    private Integer populationMax;

    @JsonProperty("AREA_CONGEST_LVL")
    private String areaCongestLvl;

    @JsonProperty("AREA_CONGEST_MSG")
    private String areaCongestMsg;

    @JsonProperty("MALE_PPLTN_RATE")
    private double malePPLTNRATE;

    @JsonProperty("FEMALE_PPLTN_RATE")
    private double femalePPLTNRATE;

    @JsonProperty("PPLTN_RATE_0")
    private double PPLTNRATE0;

    @JsonProperty("PPLTN_RATE_10")
    private double PPLTNRATE10;

    @JsonProperty("PPLTN_RATE_20")
    private double PPLTNRATE20;

    @JsonProperty("PPLTN_RATE_30")
    private double PPLTNRATE30;

    @JsonProperty("PPLTN_RATE_40")
    private double PPLTNRATE40;

    @JsonProperty("PPLTN_RATE_50")
    private double PPLTNRATE50;

    @JsonProperty("PPLTN_RATE_60")
    private double PPLTNRATE60;

    @JsonProperty("PPLTN_RATE_70")
    private double PPLTNRATE70;

    @JsonProperty("FCST_PPLTN")
    private List<PopulationForecastDto> populationForecastDto;
}
