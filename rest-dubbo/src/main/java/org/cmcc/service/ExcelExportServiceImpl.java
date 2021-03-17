package org.cmcc.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.dubbo.config.annotation.Service;
import org.cmcc.dao.CustExcelEntityDao;
import org.cmcc.entity.ExcelEntity;
import org.cmcc.service.dto.ExcelEntityDto;
import org.cmcc.utils.ExcelEntity2Dto;
import org.cmcc.utils.ExportExcelUtil;
import org.cmcc.utils.SFTPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service(protocol = {"dubbo", "rest"})
@Path("/excel")
@CrossOrigin(origins = "*")
@Slf4j
public class ExcelExportServiceImpl implements ExcelExportService {

    @Autowired
    private CustExcelEntityDao custExcelEntityDao;
    //    private String separator = File.separator;
    private String separator = "/";
    private String path = "D://";

    @Override
    @Path("export/{tableName}")
    @POST
    @Produces("application/json")
    public List<Map<String, Object>> excelExport(@PathParam("tableName") String tableName, @RequestParam(value = "colms[]",required = false) String[] colms) {
        ExcelEntity excelEntity = custExcelEntityDao.findTableSql(tableName);
        ExcelEntityDto excelEntityDto;
        if (colms != null && colms.length != 0) {
            excelEntityDto = ExcelEntity2Dto.excel2Dto(excelEntity, colms);
        } else {
            excelEntityDto = ExcelEntity2Dto.excel2Dto(excelEntity);
        }
        LinkedHashMap<String, String> colmsMap = excelEntityDto.getColms();
        List<Map<String, Object>> data = custExcelEntityDao.findByTableNameAndColms(tableName, colmsMap);
        new ExportExcelUtil().exportData("", null, colmsMap, data, path + tableName + ".xlsx");
        return data;
    }

    @Override
    @Path("list")
    @GET
    @Produces("application/json")
    public LinkedHashMap<String, ExcelEntityDto> showTables() {
        return custExcelEntityDao.showTables();
    }

    @Override
    public String check(String dir, String username, String host, String port, String pwd) {
        if (checkConn(dir, username, host, port, pwd)) {
            return "连接测试成功";
        } else {
            return "连接测试失败:创建远程目录失败";
        }
    }

    @Override
    public String upload(String tableName, String[] colms, String dir, String username, String host, String port, String pwd) {
        List<Map<String, Object>> maps = excelExport(tableName, colms);
        log.info(maps.toString());
        createRemoteDir(dir, username, host, port, pwd);
        //批量上传&&上传之后删除本地文件
        try {
            String[] tbn = {tableName + ".xlsx"};
            if (isBatchUpOrDownload(dir, username, host, port, pwd, tbn)) {
                FileUtils.deleteDirectory(new File(tbn[0]));
                return "上传成功";
            } else {
                return "上传失败";
            }
        } catch (IOException e) {
            log.info(e.getMessage());
            return "上传失败：" + e.getMessage();
        }
    }

    private boolean isBatchUpOrDownload(String dir, String userName, String host, String port, String password, String[] fileNames) {
        return SFTPUtils.batchUpOrDownload(
                userName, host, port, password, null,
                dir + separator,
                path + separator,
                SFTPUtils.OPERATE_UPLOAD,
                fileNames);
    }

    private boolean createRemoteDir(String dir, String userName, String host, String port, String password) {
        return SFTPUtils.operatesftp(userName, host, port, password, null,
                dir,
                null,
                null,
                null,
                SFTPUtils.OPERATE_MKDIR);
    }

    private boolean checkConn(String dir, String userName, String host, String port, String password) {
        return SFTPUtils.operatesftp(userName, host, port, password, null,
                dir,
                null,
                null,
                null,
                SFTPUtils.OPERATE_CHECK);
    }

}
