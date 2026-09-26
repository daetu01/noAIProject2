package com.city.monitoring.city.service;

import com.city.monitoring.city.domain.Area;
import com.city.monitoring.city.dto.AreaDto;
import com.city.monitoring.city.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final AreaRepository areaRepository;

    public List<AreaDto.AreaResponse> getAreaList() {
        List<Area> areas = areaRepository.findAll();

        return areas.stream()
                .map(area -> AreaDto
                        .AreaResponse
                        .builder()
                        .id(area.getId())
                        .areaId(area.getAreaId())
                        .areaName(area.getAreaName())
                        .category(area.getCategory())
                        .build())
                .toList();
    }
}
