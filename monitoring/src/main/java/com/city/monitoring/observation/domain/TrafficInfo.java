package com.city.monitoring.observation.domain;

import com.city.monitoring.city.domain.Area;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrafficInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Area area;

    private String roadMsg;

    private int roadTrafficSpeed;
}
