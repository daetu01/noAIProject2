package com.city.monitoring.observation.dto.seoul124Api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoadTraffic {
    @JsonProperty("AVG_ROAD_DATA")
    private AvgRoadData avgRoadData;

    @Getter
    public static class AvgRoadData {
        @JsonProperty("ROAD_MSG")
        private String roadMsg;

        @JsonProperty("ROAD_TRAFFIC_SPD")
        private Integer roadTrafficSpeed;

        @JsonProperty("ROAD_TRAFFIC_IDX")
        private String roadTrafficIdx;

        @JsonProperty("ROAD_TRAFFIC_TIME")
        private String roadTrafficTime;
    }
    @JsonProperty("ROAD_TRAFFIC_STTS")
    List<RoadTrafficSTT> roadTrafficSTTList;
}
