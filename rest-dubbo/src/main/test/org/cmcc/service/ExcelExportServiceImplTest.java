package org.cmcc.service;

import lombok.extern.slf4j.Slf4j;
import org.cmcc.service.dto.ExcelEntityDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ExcelExportServiceImplTest {

    @Autowired
    private ExcelExportServiceImpl excelExportService;

    @Test
    public void excelExport() {
        String[] colms = new String[3];
        colms[0] = "id";
        colms[1] = "table_sql";
        colms[2] = "table_name";
        List<Map<String, Object>> excel_entity = excelExportService.excelExport("excel_entity", colms);
        System.out.println(excel_entity);
    }

    @Test
    public void showTables() {
        LinkedHashMap<String, ExcelEntityDto> s = excelExportService.showTables("");
        System.out.println(s);
    }
}
