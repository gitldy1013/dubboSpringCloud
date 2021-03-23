package org.cmcc.service;

import org.cmcc.service.dto.ExcelEntityDto;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ExcelExportService {
    public List<Map<String, Object>> excelExport(String tableName, String... colms);

    public LinkedHashMap<String, ExcelEntityDto> showTables(String tableName);

    public List<String> tableList(String tableName);

    public String check(String dir, String username, String host, String port, String pwd);

    public String upload(String tableName, String[] colms, String dir, String username, String host, String port, String pwd);

    public byte[] getFile(String filename, String[] colms) throws IOException;

}
