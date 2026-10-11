package com.city.monitoring.observation.service;

import com.city.monitoring.city.domain.Area;
import com.city.monitoring.city.repository.AreaRepository;
import com.city.monitoring.collector.client.SeoulOpenDataClient;
import com.city.monitoring.global.config.domain.AcceptRecord;
import com.city.monitoring.global.config.domain.TransmissionRecord;
import com.city.monitoring.global.config.repository.AcceptRecordRepository;
import com.city.monitoring.global.config.repository.TransmissionRecordRepository;
import com.city.monitoring.observation.domain.*;
import com.city.monitoring.observation.dto.PopulationDto;
import com.city.monitoring.observation.dto.TrafficDto;
import com.city.monitoring.observation.dto.WeatherDto;
import com.city.monitoring.observation.dto.areaApi.AreaResponse;
import com.city.monitoring.observation.dto.seoul124Api.*;
import com.city.monitoring.observation.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObservationService {
    private static LocalDateTime curTime;
    private final AreaRepository areaRepository;
    private final TrafficInfoRepository trafficInfoRepository;
    private final PopulationInfoRepository populationInfoRepository;
    private final WeatherInfoRepository weatherInfoRepository;

    private final AcceptRecordRepository acceptRecordRepository;

    private final SeoulOpenDataClient client;

    @Transactional
    public SeoulDataResponse getSeoulDataArea(ObservationDto.RequestSeoulDataByArea reqData) {
        // 해당 시간대 데이터가 이미 존재한다면 건너 뛰어야 함.
        // 데이터 저장 방식을 바꿔야함.
        // API 결과 분석해서 저장방식을 바꿔야될 듯.
        curTime = LocalDateTime.now()
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        Area area = areaRepository.findByAreaName(reqData.getAreaName())
                .orElseThrow(() -> new IllegalArgumentException("없는 장소정보입니다."));

        if (weatherInfoRepository.existsByAreaAndReferenceTime(
                area,
                curTime
        )) {
            // 3개로 ?

            return null;
        }
        SeoulDataResponse response = client.getSeoulDataArea(reqData.getAreaName());

        // 수신 기록 저장
        AcceptRecord acceptRecord = AcceptRecord
                .builder()
                .resultCode(response.getResult().getCode())
                .resultMsg(response.getResult().getMessage())
                .acceptTime(LocalDateTime.now())
                .build();

        // 날씨.
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
                .referenceTime(curTime)
                .pm10Index(seoulWeather.getPm10Index())
                .pm25Index(seoulWeather.getPm25Index())
                .uvMsg(seoulWeather.getUvMsg())
                .uvIndex(seoulWeather.getUvIndex())
                .uvIndexLevel(seoulWeather.getUvIndexLvl())
                .build();

        // traffic
        RoadTraffic.AvgRoadData avgRoadData = response.getCityData().getRoadTraffic().getAvgRoadData();

        TrafficInfo trafficInfo = TrafficInfo.builder()
                .area(area)
                .roadTrafficSpeed(avgRoadData.getRoadTrafficSpeed())
                .referenceTime(curTime)
                .roadMsg(avgRoadData.getRoadMsg())
                .trafficSTTList(new ArrayList<>())
                .build();

        List<TrafficSTT> trafficSTTList = new ArrayList<>();
        for (RoadTrafficSTT road : response.getCityData().getRoadTraffic().getRoadTrafficSTTList()) {

            TrafficSTT trafficSTT = TrafficSTT.builder()
                    .linkId(road.getLinkId())
                    .idx(road.getIdx())
                    .trafficInfo(trafficInfo)
                    .dist(road.getDist())
                    .speed(road.getSpeed())
                    .roadNm(road.getRoadNm())
                    .startNdCd(road.getStartNdCd())
                    .startNdNm(road.getStartNdNm())
                    .startNdX(Float.parseFloat(road.getStartNdXY().split("_")[0]))
                    .startNdY(Float.parseFloat(road.getStartNdXY().split("_")[1]))
                    .endNdCd(road.getEndNdCd())
                    .endNdNm(road.getEndNdNm())
                    .endNdX(Float.parseFloat(road.getEndNdXY().split("_")[0]))
                    .endNdY(Float.parseFloat(road.getEndNdXY().split("_")[1]))
                    .xyList(new ArrayList<>())
                    .build();


            for (String pair : road.getXyList().split("\\|")) {

                String[] xy = pair.split("_");

                TrafficSTTXY point = TrafficSTTXY.builder()
                        .x(Float.parseFloat(xy[0]))
                        .y(Float.parseFloat(xy[1]))
                        .build();

                trafficSTT.addXY(point);
            }

            trafficSTTList.add(trafficSTT);
        }

        trafficInfo.setTrafficSTTList(trafficSTTList);

        // PPLTN Data
        LivePopulation livePopulation = response.getCityData().getLivePopulation().get(0);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        PopulationInfo populationInfo = PopulationInfo.builder()
                .popluationMax(livePopulation.getPopulationMax())
                .popluationMin(livePopulation.getPopulationMin())
                .areaCongestMsg(livePopulation.getAreaCongestMsg())
                .area(area)
                .femalePPLTNRate(livePopulation.getFemalePPLTNRATE())
                .malePPLTNRate(livePopulation.getMalePPLTNRATE())
                .PPLTNRate0(livePopulation.getPPLTNRATE0())
                .PPLTNRate10(livePopulation.getPPLTNRATE10())
                .PPLTNRate20(livePopulation.getPPLTNRATE20())
                .PPLTNRate30(livePopulation.getPPLTNRATE30())
                .PPLTNRate40(livePopulation.getPPLTNRATE40())
                .PPLTNRate50(livePopulation.getPPLTNRATE50())
                .PPLTNRate60(livePopulation.getPPLTNRATE60())
                .PPLTNRate70(livePopulation.getPPLTNRATE70())
                .populationForecastInfo(livePopulation.getPopulationForecastDto().stream().map(
                        populationForecastDto -> PopulationForecastInfo
                                .builder()
                                .fcstCongestLvl(populationForecastDto.getFcstCongestLvl())
                                .fcstPPLTNMAX(populationForecastDto.getFcstPPLTNMAX())
                                .fcstTime(
                                        LocalDateTime.parse(populationForecastDto.getFcstTime(),
                                                formatter2)
                                )
                                .fcstPPLTNMIN(populationForecastDto.getFcstPPLTNMIN())
                                .build()).toList())
                .build();


        populationInfoRepository.save(populationInfo);

        WeatherInfo wInfo = weatherInfoRepository.save(weatherInfo);

        wInfo.setWeatherForecastInfo(seoulWeather.getFcst24hours().stream().map(
                        weatherForecastDto ->
                                WeatherForecastInfo
                                        .builder()
                                        .weatherInfo(wInfo)
                                        .temperature(weatherForecastDto.getTemp())
                                        .stystts(weatherForecastDto.getSkyStts())
                                        .rainChance(weatherForecastDto.getRainChance())
                                        .build())
                .toList());

        // 교통 기록 저장
        trafficInfoRepository.save(trafficInfo);


        // 통신 기록 저장
        acceptRecordRepository.save(acceptRecord);

        return response;
    }

    public SeoulDataResponse getSeoulDataIndex(ObservationDto.RequestSeoulDataByTime reqData) {
        SeoulDataResponse response = client.getSeoulDataIndex(reqData.getFrom(), reqData.getTo(), reqData.getAreaName());

        curTime = LocalDateTime.now()
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        // weather
        WeatherInfo weatherInfo = WeatherInfo
                .builder()
                .maxTemp(response.getCityData().getWeatherList().get(0).getMaxTemp())
                .minTemp(response.getCityData().getWeatherList().get(0).getMinTemp())
                .temperature(response.getCityData().getWeatherList().get(0).getTemperature())
                .referenceTime(curTime)
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

    // 현재 시간대 혼잡도 조사 API
    public AreaResponse.Get getAreaResponse(Long id) {

        curTime = LocalDateTime.now()
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 장소입니다."));

        WeatherInfo weatherInfo = weatherInfoRepository.findByAreaAndReferenceTime(area, curTime)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장소입니다."));

        TrafficInfo trafficInfo = trafficInfoRepository.findByAreaAndReferenceTime(area, curTime)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장소입니다."));

        PopulationInfo populationInfo = populationInfoRepository.findByAreaAndReferenceTime(area, curTime)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장소입니다."));

        return AreaResponse.Get
                .builder()
                .id(id)
                .areaCode(area.getAreaId())
                .cityName(area.getAreaName())
                .populationDto(
                        PopulationDto
                                .builder()
                                .popluationMax(populationInfo.getPopluationMax())
                                .popluationMin(populationInfo.getPopluationMin())
                                .areaCongestMsg(populationInfo.getAreaCongestMsg())
                                .femalePPLTNRate(populationInfo.getFemalePPLTNRate())
                                .malePPLTNRate(populationInfo.getMalePPLTNRate())
                                .PPLTNRate0(populationInfo.getPPLTNRate0())
                                .PPLTNRate10(populationInfo.getPPLTNRate10())
                                .PPLTNRate20(populationInfo.getPPLTNRate20())
                                .PPLTNRate30(populationInfo.getPPLTNRate30())
                                .PPLTNRate40(populationInfo.getPPLTNRate40())
                                .PPLTNRate50(populationInfo.getPPLTNRate50())
                                .PPLTNRate60(populationInfo.getPPLTNRate60())
                                .PPLTNRate70(populationInfo.getPPLTNRate70())
                                .build()
                )
                .trafficDto(
                        TrafficDto
                                .builder()
                                .roadMsg(trafficInfo.getRoadMsg())
                                .roadTrafficSpeed(trafficInfo.getRoadTrafficSpeed())
                                .build()
                )
                .weatherDto(
                        WeatherDto
                                .builder()
                                .pm10(weatherInfo.getPm10())
                                .pm25(weatherInfo.getPm25())
                                .pcpMsg(weatherInfo.getPcpMsg())
                                .pm25Index(weatherInfo.getPm25Index())
                                .sunrise(weatherInfo.getSunrise())
                                .sunset(weatherInfo.getSunset())
                                .uvIndex(weatherInfo.getUvIndex())
                                .referenceTime(weatherInfo.getReferenceTime())
                                .temperature(weatherInfo.getTemperature())
                                .maxTemp(weatherInfo.getMaxTemp())
                                .minTemp(weatherInfo.getMinTemp())
                                .uvMsg(weatherInfo.getUvMsg())
                                .pcpMsg(weatherInfo.getPcpMsg())
                                .uvIndexLevel(weatherInfo.getUvIndexLevel())
                                .build()
                )
                .build();
    }
}
