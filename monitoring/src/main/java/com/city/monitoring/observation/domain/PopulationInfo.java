package com.city.monitoring.observation.domain;

import com.city.monitoring.city.domain.Area;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PopulationInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Area area;

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

    @OneToMany(mappedBy = "populationInfo", cascade = CascadeType.PERSIST)
    private List<PopulationForecastInfo> populationForecastInfo;

    private LocalDateTime referenceTime;
}
