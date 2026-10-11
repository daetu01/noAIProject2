package com.city.monitoring.observation.dto.seoul124Api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class SeoulWeather {

    @JsonProperty("TEMP")
    private Double temperature;

    @JsonProperty("MAX_TEMP")
    private Double maxTemp;

    @JsonProperty("MIN_TEMP")
    private Double minTemp;

    @JsonProperty("PCP_MSG")
    private String pcpMsg;

    @JsonProperty("SUNRISE")
    private String sunrise;

    @JsonProperty("SUNSET")
    private String sunset;

    @JsonProperty("UV_INDEX_LVL")
    private String uvIndexLvl;

    @JsonProperty("UV_INDEX")
    private int uvIndex;

    @JsonProperty("UV_MSG")
    private String uvMsg;

    @JsonProperty("PM25_INDEX")
    private String pm25Index;

    @JsonProperty("PM25")
    private int pm25;

    @JsonProperty("PM10_INDEX")
    private String pm10Index;

    @JsonProperty("PM10")
    private int pm10;

    @JsonProperty("FCST24HOURS")
    private List<WeatherForecastDto> fcst24hours;
}
