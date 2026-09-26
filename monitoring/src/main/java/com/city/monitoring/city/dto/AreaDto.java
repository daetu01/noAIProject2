package com.city.monitoring.city.dto;

import com.city.monitoring.observation.domain.Category;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AreaDto {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AreaResponse {
        private Long id;

        private Category category;

        private String areaId;

        private String areaName;

        private String englishName;
    }
}
