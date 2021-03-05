package org.cmcc.service;

import org.apache.dubbo.config.annotation.Service;
import org.cmcc.dao.CustExcelEntityDao;
import org.cmcc.dto.ExcelEntityDto;
import org.cmcc.entity.ExcelEntity;
import org.cmcc.utils.ExcelEntity2Dto;
import org.cmcc.utils.ExportExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(protocol = "rest")
@Path("/excel")
public class ExcelExportServiceImpl implements ExcelExportService {

    @Autowired
    private CustExcelEntityDao custExcelEntityDao;

    @Override
    @Path("export/{tableName}")
    @POST
    public String excelExport(@PathParam("tableName") String tableName, @RequestParam("colms[]") String... colms) {
        List<ExcelEntity> tableSql = custExcelEntityDao.findTableSql(tableName);
        ExcelEntity excelEntity = tableSql.get(0);
        ExcelEntityDto excelEntityDto = ExcelEntity2Dto.excel2Dto(excelEntity);
        LinkedHashMap<String, String> collect = (LinkedHashMap<String, String>) excelEntityDto.getColms().entrySet().stream().filter((e) -> Arrays.asList(colms).contains(e.getKey())).collect(Collectors.toMap(
                (e) -> (String) e.getKey(),
                Map.Entry::getValue
        ));
        LinkedHashMap<String, String> hashMap = collect;
        List<Map<String, Object>> data = custExcelEntityDao.findByTableNameAndColms(tableName, hashMap);
        new ExportExcelUtil().exportData("", null, hashMap, data, "D://" + tableName + ".xls");
        return data.toString();
    }

}
