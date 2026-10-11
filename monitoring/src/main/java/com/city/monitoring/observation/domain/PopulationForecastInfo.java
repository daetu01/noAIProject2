package com.city.monitoring.observation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PopulationForecastInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fcstTime;

    private String fcstCongestLvl;

    private int fcstPPLTNMIN;

    private int fcstPPLTNMAX;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "population_info_id")
    private PopulationInfo populationInfo;
}
