package com.city.monitoring.observation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrafficDto {
    private String roadMsg;

    private int roadTrafficSpeed;
}
