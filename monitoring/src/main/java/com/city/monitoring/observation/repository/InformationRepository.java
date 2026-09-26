package com.city.monitoring.observation.repository;

import com.city.monitoring.observation.domain.AreaInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationRepository extends JpaRepository<AreaInfo, Long> {
}
