package org.cmcc.utils;

import lombok.extern.slf4j.Slf4j;
import org.cmcc.dto.ExcelEntityDto;
import org.cmcc.entity.ExcelEntity;

import java.util.LinkedHashMap;

@Slf4j
public class ExcelEntity2Dto {

    public static ExcelEntityDto excel2Dto(ExcelEntity excelEntity) {
        String tableSql = excelEntity.getTableSql();
        String colmsStr = tableSql.substring(tableSql.indexOf("(") + 1, tableSql.lastIndexOf(")"));
        String[] colms = colmsStr.split(",");
        ExcelEntityDto excelEntityDto = new ExcelEntityDto();
        LinkedHashMap<String, String> colmsMap = new LinkedHashMap<>();
        for (int i = 0; i < colms.length; i++) {
            String key = colms[i].substring(colms[i].indexOf("`") + 1, colms[i].lastIndexOf("`"));
            String value = colms[i].substring(colms[i].indexOf("'") + 1, colms[i].lastIndexOf("'"));
            colmsMap.put(key, value);
        }
        excelEntityDto.setTableName(excelEntity.getTableName());
        excelEntityDto.setColms(colmsMap);
        return excelEntityDto;
    }

}
