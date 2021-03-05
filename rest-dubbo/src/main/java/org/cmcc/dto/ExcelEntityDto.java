package org.cmcc.dto;

import lombok.Data;
import lombok.ToString;

import java.util.LinkedHashMap;

@Data
@ToString
public class ExcelEntityDto {
    private String tableName;

    private LinkedHashMap<String, String> colms;

}

