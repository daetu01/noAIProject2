package com.city.monitoring.global.config;

import com.city.monitoring.global.config.service.AreaExcelImportService;
import com.city.monitoring.city.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class AreaDataInitializer implements CommandLineRunner {

    private final AreaExcelImportService importService;
    private final AreaRepository areaRepository;

    @Override
    public void run(String... args) throws Exception {

        if (areaRepository.count() > 0) {
            return;
        }

        File file = new File("src/main/resources/data/seoulSpot121.xlsx");

        importService.importExcel(file);
    }
}
