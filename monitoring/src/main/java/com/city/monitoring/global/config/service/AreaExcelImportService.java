package com.city.monitoring.global.config.service;

import com.city.monitoring.city.domain.Area;
import com.city.monitoring.observation.domain.Category;
import com.city.monitoring.city.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaExcelImportService {

    private final AreaRepository areaRepository;

    public void importExcel(File file) throws IOException {
        List<Area> areas = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum() ; i++) {
                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                Cell areaCodeCell = row.getCell(2);

                if (areaCodeCell == null) {
                    continue;
                }

                String areaCode = areaCodeCell.toString().trim();

                if (areaCode.isEmpty()) {
                    continue;
                }

                String categoryValue = row.getCell(0).getStringCellValue();
                String areaName = row.getCell(3).getStringCellValue();
                String englishName = row.getCell(4).getStringCellValue();

                Category category = Category.fromLabel(categoryValue);

                Area area = new Area(
                        category,
                        areaCode,
                        areaName,
                        englishName
                );

                areas.add(area);
            }

            areaRepository.saveAll(areas);
        }

    }
}
