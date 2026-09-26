package com.city.monitoring.observation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class SeoulCityData {
    @JsonProperty("AREA_NM")
    private String areaName;

    @JsonProperty("AREA_CD")
    private String areaCode;

    @JsonProperty("LIVE_PPLTN_STTS")
    private List<LivePopulation> livePopulation;

    @JsonProperty("ROAD_TRAFFIC_STTS")
    private RoadTraffic roadTraffic;

    @JsonProperty("WEATHER_STTS")
    private List<SeoulWeather> weatherList;
}
