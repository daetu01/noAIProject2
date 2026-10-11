package com.city.monitoring.global.config.repository;

import com.city.monitoring.global.config.domain.TransmissionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransmissionRecordRepository extends JpaRepository<TransmissionRecord, Long> {
}
