package com.city.monitoring.observation.repository;

import com.city.monitoring.observation.domain.TrafficSTTXY;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficSTTXYRepository extends JpaRepository<TrafficSTTXY, Long> {
}
