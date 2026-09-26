package com.city.monitoring.observation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RoadTraffic {
    @JsonProperty("AVG_ROAD_DATA")
    private AvgRoadData avgRoadData;

    @Getter
    public static class AvgRoadData {
        @JsonProperty("ROAD_MSG")
        private String roadMsg;

        @JsonProperty("ROAD_TRAFFIC_SPD")
        private Integer roadTrafficSpeed;
    }
}
