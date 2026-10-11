package com.city.monitoring.observation.repository;

import com.city.monitoring.city.domain.Area;
import com.city.monitoring.observation.domain.TrafficInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TrafficInfoRepository extends JpaRepository<TrafficInfo, Long> {
    Optional<TrafficInfo> findByAreaAndReferenceTime(Area area, LocalDateTime curTime);
}
