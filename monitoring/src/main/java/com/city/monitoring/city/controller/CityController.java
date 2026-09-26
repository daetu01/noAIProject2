package com.city.monitoring.city.controller;

import com.city.monitoring.city.dto.AreaDto;
import com.city.monitoring.city.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/city")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping("/area")
    public ResponseEntity<List<AreaDto.AreaResponse>> getAreaList() {
        return ResponseEntity.ok(cityService.getAreaList());
    }
}
