package com.city.monitoring.city.domain;

import com.city.monitoring.observation.domain.Category;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 추후에 추가할 예정
//    @ManyToOne
//    private Dong dong;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    private String areaId;

    private String areaName;

    private String englishName;

    public Area (Category category, String areaCode, String areaName, String englishName) {
        this.category = category;
        this.areaId = areaCode;
        this.areaName = areaName;
        this.englishName = englishName;
    }
}
