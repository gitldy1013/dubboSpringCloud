package org.cmcc.service.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedHashMap;

@Data
@ToString
public class ExcelEntityDto implements Serializable {
    private String tableName;

    private LinkedHashMap<String, String> colms;

}

