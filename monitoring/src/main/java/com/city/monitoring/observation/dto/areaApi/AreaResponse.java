package com.city.monitoring.observation.dto.areaApi;

import com.city.monitoring.observation.dto.PopulationDto;
import com.city.monitoring.observation.dto.TrafficDto;
import com.city.monitoring.observation.dto.WeatherDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AreaResponse {


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Get {
        private Long id;
        private String cityName;
        private String areaCode;

        // 들어가야될 정보. 날씨, 교통, 인구
        private PopulationDto populationDto;

        private TrafficDto trafficDto;

        private WeatherDto weatherDto;
    }
}
