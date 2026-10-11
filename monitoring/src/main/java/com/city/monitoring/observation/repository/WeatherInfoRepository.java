package com.city.monitoring.observation.repository;

import com.city.monitoring.city.domain.Area;
import com.city.monitoring.observation.domain.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, Long> {
    // 시간대 별로 데이터를 조회할 수 있어야 함.
    boolean existsByAreaAndReferenceTime(
            Area area,
            LocalDateTime referenceTime
    );

    Optional<WeatherInfo> findByAreaAndReferenceTime(Area area, LocalDateTime referenceTime);
}
