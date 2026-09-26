package com.city.monitoring.observation.service;

import com.city.monitoring.city.domain.Area;
import com.city.monitoring.city.repository.AreaRepository;
import com.city.monitoring.collector.client.SeoulOpenDataClient;
import com.city.monitoring.observation.domain.AreaInfo;
import com.city.monitoring.observation.domain.TrafficInfo;
import com.city.monitoring.observation.domain.WeatherForecastInfo;
import com.city.monitoring.observation.domain.WeatherInfo;
import com.city.monitoring.observation.dto.ObservationDto;
import com.city.monitoring.observation.dto.SeoulDataResponse;
import com.city.monitoring.observation.dto.SeoulWeather;
import com.city.monitoring.observation.repository.InformationRepository;
import com.city.monitoring.observation.repository.SpotRepository;
import com.city.monitoring.observation.repository.TrafficInfoRepository;
import com.city.monitoring.observation.repository.WeatherInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ObservationService {
    private static LocalDateTime curTime;
    private final AreaRepository areaRepository;
    private final TrafficInfoRepository trafficInfoRepository;
    private final WeatherInfoRepository weatherInfoRepository;

    private final SeoulOpenDataClient client;

    public SeoulDataResponse getSeoulDataArea(ObservationDto.RequestSeoulDataByArea reqData) {
        // 해당 시간대 데이터가 이미 존재한다면 건너 뛰어야 함.
        // 데이터 저장 방식을 바꿔야함.
        // API 결과 분석해서 저장방식을 바꿔야될 듯.
        curTime = LocalDateTime.now();

        Area area = areaRepository.findByAreaName(reqData.getAreaName())
                .orElseThrow(() -> new IllegalArgumentException("없는 장소정보입니다."));

        if (weatherInfoRepository.existsByAreaAndReferenceTime(
                area,
                curTime
        )) {
            return null;
        }
        SeoulDataResponse response = client.getSeoulDataArea(reqData.getAreaName());

        SeoulWeather seoulWeather = response.getCityData().getWeatherList().get(0);

        LocalDate date = LocalDate.now();

        LocalTime time = LocalTime.parse(seoulWeather.getSunrise());

        LocalDateTime sunrise = LocalDateTime.of(date, time);
        LocalDateTime sunset = LocalDateTime.of(date, time);
        // weather
        WeatherInfo weatherInfo = WeatherInfo
                .builder()
                .area(area)
                .maxTemp(seoulWeather.getMaxTemp())
                .minTemp(seoulWeather.getMinTemp())
                .temperature(seoulWeather.getTemperature())
                .referenceTime(LocalDateTime.now())
                .pcpMsg(seoulWeather.getPcpMsg())
                .pm10(seoulWeather.getPm10())
                .pm25(seoulWeather.getPm25())
                .sunrise(sunrise)
                .sunset(sunset)
                .pm10Index(seoulWeather.getPm10Index())
                .pm25Index(seoulWeather.getPm25Index())
                .uvMsg(seoulWeather.getUvMsg())
                .uvIndex(seoulWeather.getUvIndex())
                .uvIndexLevel(seoulWeather.getUvIndexLvl())
                .weatherForecastInfo(seoulWeather.getFcst24hours().stream().map(
                        weatherForecastDto ->
                                WeatherForecastInfo
                                        .builder()
                                        .temperature(weatherForecastDto.getTemp())
                                        .stystts(weatherForecastDto.getSkyStts())
                                        .rainChance(weatherForecastDto.getRainChance())
                                        .build())
                        .toList())
                .build();

        // traffic
        TrafficInfo trafficInfo = TrafficInfo
                .builder()
                .roadTrafficSpeed(response.getCityData().getRoadTraffic().getAvgRoadData().getRoadTrafficSpeed())
                .roadMsg(response.getCityData().getRoadTraffic().getAvgRoadData().getRoadMsg())
                .build();

        weatherInfoRepository.save(weatherInfo);
        trafficInfoRepository.save(trafficInfo);

        return response;
    }

    public SeoulDataResponse getSeoulDataIndex(ObservationDto.RequestSeoulDataByTime reqData) {
        SeoulDataResponse response = client.getSeoulDataIndex(reqData.getFrom(), reqData.getTo(), reqData.getAreaName());

        // Area
        AreaInfo areaInfo = AreaInfo
                .builder()

                .build();

        // weather
        WeatherInfo weatherInfo = WeatherInfo
                .builder()
                .maxTemp(response.getCityData().getWeatherList().get(0).getMaxTemp())
                .minTemp(response.getCityData().getWeatherList().get(0).getMinTemp())
                .temperature(response.getCityData().getWeatherList().get(0).getTemperature())
                .referenceTime(LocalDateTime.now())
                .pcpMsg(response.getCityData().getWeatherList().get(0).getPcpMsg())
                .build();

        // traffic
        TrafficInfo trafficInfo = TrafficInfo
                .builder()
                .roadTrafficSpeed(response.getCityData().getRoadTraffic().getAvgRoadData().getRoadTrafficSpeed())
                .roadMsg(response.getCityData().getRoadTraffic().getAvgRoadData().getRoadMsg())
                .build();

        weatherInfoRepository.save(weatherInfo);
        trafficInfoRepository.save(trafficInfo);

        return response;
    }
}
