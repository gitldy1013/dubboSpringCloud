package org.cmcc.service;

import org.apache.dubbo.config.annotation.Service;
import org.cmcc.dao.CustExcelEntityDao;
import org.cmcc.dto.ExcelEntityDto;
import org.cmcc.entity.ExcelEntity;
import org.cmcc.utils.ExcelEntity2Dto;
import org.cmcc.utils.ExportExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service(protocol = "rest")
@Path("/excel")
public class ExcelExportServiceImpl implements ExcelExportService {

    @Autowired
    private CustExcelEntityDao custExcelEntityDao;

    @Override
    @Path("export/{tableName}")
    @POST
    public String excelExport(@PathParam("tableName") String tableName, @RequestBody(required = false) String... colms) {
        ExcelEntity excelEntity = custExcelEntityDao.findTableSql(tableName);
        ExcelEntityDto excelEntityDto;
        if (colms != null && colms.length != 0) {
            excelEntityDto = ExcelEntity2Dto.excel2Dto(excelEntity, colms);
        } else {
            excelEntityDto = ExcelEntity2Dto.excel2Dto(excelEntity);
        }
        LinkedHashMap<String, String> colmsMap = excelEntityDto.getColms();
        List<Map<String, Object>> data = custExcelEntityDao.findByTableNameAndColms(tableName, colmsMap);
        new ExportExcelUtil().exportData("", null, colmsMap, data, "D://" + tableName + ".xlsx");
        return data.toString();
    }

    @Path("/list")
    @GET
    public LinkedHashMap<String, ExcelEntityDto> showTables() {
        LinkedHashMap<String, ExcelEntityDto> excelEntities = custExcelEntityDao.showTables();
        return excelEntities;
    }

}
