package org.cmcc.dao;

import lombok.extern.slf4j.Slf4j;
import org.cmcc.entity.EntitySftpSql;
import org.cmcc.entity.ExcelEntity;
import org.cmcc.service.dto.EntitySftpSqlDto;
import org.cmcc.service.dto.ExcelEntityDto;
import org.cmcc.utils.ExcelEntity2Dto;
import org.cmcc.utils.ExportExcelUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private EntitySftpSqlDao entitySftpSqlDao;

    @Test
    public void findTableSql() {
        ExcelEntity tableSql = excelEntityDao.findTableSql("excel_entity");
        EntitySftpSql entitySftpSqlByTableName = entitySftpSqlDao.findEntitySftpSqlByTableName("excel_entity");
        EntitySftpSqlDto entitySftpSqlDto = new EntitySftpSqlDto();
        if (entitySftpSqlByTableName != null) {
            BeanUtils.copyProperties(entitySftpSqlByTableName, entitySftpSqlDto);
        }
        ExcelEntityDto excelEntityDto = ExcelEntity2Dto.excel2Dto(tableSql, entitySftpSqlDto);
        System.out.println(excelEntityDto);
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
        ExcelEntity tableSql = excelEntityDao.findTableSql("excel_entity");
        EntitySftpSql entitySftpSqlByTableName = entitySftpSqlDao.findEntitySftpSqlByTableName("excel_entity");
        EntitySftpSqlDto entitySftpSqlDto = new EntitySftpSqlDto();
        if (entitySftpSqlByTableName != null) {
            BeanUtils.copyProperties(entitySftpSqlByTableName, entitySftpSqlDto);
        }
        ExcelEntityDto excelEntityDto = ExcelEntity2Dto.excel2Dto(tableSql, entitySftpSqlDto);
        LinkedHashMap<String, String> hashMap = excelEntityDto.getColms();
        List<Map<String, Object>> data = excelEntityDao.findByTableNameAndColms("excel_entity", hashMap);
        new ExportExcelUtil().exportData("", null, hashMap, data, "D://excel_entity.xls");
    }
}
