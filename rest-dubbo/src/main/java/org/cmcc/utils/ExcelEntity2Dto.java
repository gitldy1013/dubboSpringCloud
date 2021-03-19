package org.cmcc.utils;

import lombok.extern.slf4j.Slf4j;
import org.cmcc.entity.ExcelEntity;
import org.cmcc.service.dto.ExcelEntityDto;

import java.util.LinkedHashMap;

@Slf4j
public class ExcelEntity2Dto {

    public static ExcelEntityDto excel2Dto(ExcelEntity excelEntity, String[] colmSort) {
        String tableSql = excelEntity.getTableSql();
        String colmsStr = tableSql.substring(tableSql.indexOf("(") + 1, tableSql.lastIndexOf(")"));
        String[] colms = colmsStr.split(",\n");
        ExcelEntityDto excelEntityDto = new ExcelEntityDto();
        LinkedHashMap<String, String> colmsMap = new LinkedHashMap<>();
        for (String s : colmSort) {
            for (String colm : colms) {
                String key = colm.substring(colm.indexOf("`") + 1, colm.lastIndexOf("`"));
                String value = colm.substring(colm.indexOf("COMMENT '") + 9, colm.lastIndexOf("'"));
                if (s.equals(key)) {
                    colmsMap.put(key, value);
                    break;
                }
            }
        }
        excelEntityDto.setTableName(excelEntity.getTableName());
        excelEntityDto.setColms(colmsMap);
        return excelEntityDto;
    }

    public static ExcelEntityDto excel2Dto(ExcelEntity excelEntity) {
        String tableSql = excelEntity.getTableSql();
        String colmsStr = tableSql.substring(tableSql.indexOf("(") + 1, tableSql.lastIndexOf(")"));
        String[] colms = colmsStr.split(",\n");
        ExcelEntityDto excelEntityDto = new ExcelEntityDto();
        LinkedHashMap<String, String> colmsMap = new LinkedHashMap<>();
        for (int i = 0; i < colms.length; i++) {
            if (colms[i].trim().startsWith("`")) {
                String key = colms[i].substring(colms[i].indexOf("`") + 1, colms[i].lastIndexOf("`"));
                String value = "";
                if (!colms[i].contains("COMMENT '")) {
                    value = key;
                } else {
                    value = colms[i].substring(colms[i].indexOf("COMMENT '") + 9, colms[i].lastIndexOf("'"));
                }
                colmsMap.put(key, value);
            }
        }
        excelEntityDto.setTableName(excelEntity.getTableName());
        excelEntityDto.setColms(colmsMap);
        return excelEntityDto;
    }

}
