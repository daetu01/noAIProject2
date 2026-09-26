package com.city.monitoring.observation.controller;

import com.city.monitoring.observation.dto.ObservationDto;
import com.city.monitoring.observation.dto.SeoulDataResponse;
import com.city.monitoring.collector.client.SeoulOpenDataClient;
import com.city.monitoring.observation.service.ObservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seoul")
@RequiredArgsConstructor
public class ObservationController {
    private final ObservationService observationService;

    @PostMapping("/area")
    public ResponseEntity<SeoulDataResponse> getSeoulDataByArea(@RequestBody ObservationDto.RequestSeoulDataByArea reqData) {
        return ResponseEntity.ok(observationService.getSeoulDataArea(reqData));
    }

    @PostMapping("/index")
    public ResponseEntity<SeoulDataResponse> getSeoulDataByIndex(@RequestBody ObservationDto.RequestSeoulDataByTime reqData) {
        return ResponseEntity.ok(observationService.getSeoulDataIndex(reqData));
    }
}
