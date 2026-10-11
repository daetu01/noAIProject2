package com.city.monitoring.observation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopulationDto {
    private int popluationMin;

    private int popluationMax;

    private String areaCongestMsg;

    private double malePPLTNRate;

    private double femalePPLTNRate;

    private double PPLTNRate0;

    private double PPLTNRate10;

    private double PPLTNRate20;

    private double PPLTNRate30;

    private double PPLTNRate40;

    private double PPLTNRate50;

    private double PPLTNRate60;

    private double PPLTNRate70;
}
