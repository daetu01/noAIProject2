package com.city.monitoring.observation.repository;

import com.city.monitoring.observation.domain.TrafficInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficInfoRepository extends JpaRepository<TrafficInfo, Long> {
}
