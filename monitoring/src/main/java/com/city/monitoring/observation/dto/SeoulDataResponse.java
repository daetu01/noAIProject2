package com.city.monitoring.observation.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SeoulDataResponse {
    @JsonProperty("list_total_count")
    private int listTotalCount;

    @JsonProperty("RESULT")
    private Result result;

    @JsonProperty("CITYDATA")
    private SeoulCityData cityData;

    @Getter
    public static class Result {
        @JsonProperty("RESULT.CODE")
        private String code;

        @JsonProperty("RESULT.MESSAGE")
        private String message;
    }
}
