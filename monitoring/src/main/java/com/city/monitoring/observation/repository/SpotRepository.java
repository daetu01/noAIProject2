package com.city.monitoring.observation.repository;

import com.city.monitoring.city.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotRepository extends JpaRepository<Area, Long> {
}
