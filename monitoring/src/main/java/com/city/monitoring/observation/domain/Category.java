package com.city.monitoring.observation.domain;

import lombok.Getter;

@Getter
public enum Category {
    TOURIST_ZONE("관광특구"),
    CULTURAL_HERITAGE("고궁·문화유산"),
    DENSE_POPULATION_AREA("인구밀집지역"),
    DEVELOPED_COMMERCIAL_AREA("발달상권"),
    PARK("공원");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public static Category fromLabel(String label) {
        for (Category category : values()) {
            if (category.label.equals(label)) {
                return category;
            }
        }

        throw new IllegalArgumentException(
                "존재하지 않는 Category입니다: " + label
        );

    }
}
