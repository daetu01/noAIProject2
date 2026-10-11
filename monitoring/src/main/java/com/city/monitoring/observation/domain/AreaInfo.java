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


}
