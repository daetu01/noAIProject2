package com.city.monitoring.observation.domain;

import com.city.monitoring.city.domain.Area;
import jakarta.persistence.*;
import lombok.*;


// 해당 시간대에 AreaInformation
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AreaInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Area area;

    private String areaCongestLvl;

    private String areaCongestMsg;

    private int areaPPLTNMIN;

    private int areaPPLTNMAX;

    private double malePPLTNRATE;

    private double femalePPLTNRATE;

    private double PPLTNRATE0;

    private double PPLTNRATE10;
    private double PPLTNRATE20;
    private double PPLTNRATE30;
    private double PPLTNRATE40;
    private double PPLTNRATE50;
    private double PPLTNRATE60;
    private double PPLTNRATE70;

}
