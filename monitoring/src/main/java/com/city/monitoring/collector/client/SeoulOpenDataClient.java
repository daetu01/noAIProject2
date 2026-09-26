package com.city.monitoring.collector.client;

import com.city.monitoring.observation.dto.SeoulDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class SeoulOpenDataClient {
    private final RestClient restClient;

    @Value("${api.seoul}")
    private String SEOUL_API_KEY;

    public SeoulDataResponse getSeoulDataArea(String areaName) {
        return restClient.get()
                .uri(
                        uriBuilder ->
                                uriBuilder
                                        .path("/{key}/json/citydata/1/5/{area}")
                                        .build(SEOUL_API_KEY, areaName)
                )
                .retrieve()
                .body(SeoulDataResponse.class);
    }

    public SeoulDataResponse getSeoulDataIndex(int from, int to, String area) {
        return restClient.get()
                .uri(
                        uriBuilder ->
                                uriBuilder
                                        .path("/{key}/json/citydata/{from}/{to}/{area}")
                                        .build(SEOUL_API_KEY, from, to, area)
                )
                .retrieve()
                .body(SeoulDataResponse.class);
    }
}
