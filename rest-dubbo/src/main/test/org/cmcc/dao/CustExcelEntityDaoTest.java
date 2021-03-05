package org.cmcc.dao;

import lombok.extern.slf4j.Slf4j;
import org.cmcc.dto.ExcelEntityDto;
import org.cmcc.entity.ExcelEntity;
import org.cmcc.utils.ExcelEntity2Dto;
import org.cmcc.utils.ExportExcelUtil;
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
public class CustExcelEntityDaoTest {
    @Autowired
    private CustExcelEntityDao excelEntityDao;

    @Test
    public void findTableSql() {
        List<ExcelEntity> tableSql = excelEntityDao.findTableSql("excel_entity");
        for (int i = 0; i < tableSql.size(); i++) {
            ExcelEntity excelEntity = tableSql.get(i);
            ExcelEntityDto excelEntityDto = ExcelEntity2Dto.excel2Dto(excelEntity);
            System.out.println(excelEntityDto);
        }
    }

    @Test
    public void findByTableNameAndColms() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("table_name", "表名");
        map.put("table_sql", "表sql");
        List<Map<String, Object>> excel_entity = excelEntityDao.findByTableNameAndColms("excel_entity", map);
        System.out.println(excel_entity);
    }

    @Test
    public void excelExport() {
        List<ExcelEntity> tableSql = excelEntityDao.findTableSql("excel_entity");
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        for (int i = 0; i < tableSql.size(); i++) {
            ExcelEntity excelEntity = tableSql.get(i);
            ExcelEntityDto excelEntityDto = ExcelEntity2Dto.excel2Dto(excelEntity);
            hashMap = excelEntityDto.getColms();
        }
        List<Map<String, Object>> data = excelEntityDao.findByTableNameAndColms("excel_entity", hashMap);
        new ExportExcelUtil().exportData("", null, hashMap, data, "D://excel_entity.xls");
    }
}
