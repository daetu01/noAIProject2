package com.city.monitoring.global.config.repository;

import com.city.monitoring.global.config.domain.AcceptRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcceptRecordRepository extends JpaRepository<AcceptRecord, Long> {
}
