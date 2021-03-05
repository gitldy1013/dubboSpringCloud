package org.cmcc.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ExcelExportServiceImplTest {

    @Autowired
    private ExcelExportServiceImpl excelExportService;

    @Test
    public void excelExport() {
        String[] colms = new String[1];
        colms[0] = "table_name";
        String excel_entity = excelExportService.excelExport("excel_entity", colms);
        System.out.println(excel_entity);
    }
}
