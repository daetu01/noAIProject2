package com.city.monitoring.observation.domain;

import com.city.monitoring.city.domain.Area;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Area area;

    private double temperature;

    private double maxTemp;

    private double minTemp;

    private String pcpMsg;

    private LocalDateTime referenceTime;

    private LocalDateTime sunrise;

    private LocalDateTime sunset;

    private String uvIndexLevel;

    private int uvIndex;

    private String uvMsg;

    private String pm25Index;

    private int pm25;

    private String pm10Index;

    private int pm10;

    @OneToMany(mappedBy = "weatherInfo", cascade = CascadeType.PERSIST)
    private List<WeatherForecastInfo> weatherForecastInfo;
}
