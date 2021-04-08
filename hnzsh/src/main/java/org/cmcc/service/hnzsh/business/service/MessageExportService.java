package org.cmcc.service.hnzsh.business.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.cmcc.service.hnzsh.business.dao.MessageExportMapper;
import org.cmcc.service.hnzsh.config.SftpConfig;
import org.cmcc.service.hnzsh.pojo.Hnzsh;
import org.cmcc.service.hnzsh.pojo.HnzshDto;
import org.cmcc.service.hnzsh.utils.ExcelUtils;
import org.cmcc.service.hnzsh.utils.SFTPUtils;
import org.cmcc.service.hnzsh.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Service
@Slf4j
public class MessageExportService {

    private String separator = File.separator;
//    private String separator = "/";

    @Autowired
    private SftpConfig sftpConfig;

    @Autowired
    private MessageExportMapper messageExportMapper;

    @DS("mysql")
    public void msgExport(String dateTime) {
        if (StringUtils.isEmpty(dateTime)) {
            dateTime = TimeUtils.getYestDay();
        }

        List<HnzshDto> hnzshDtos = sftpConfig.getFileNames();
        // 创建本地和远程目录
        Path path = Paths.get(sftpConfig.getModDir() + dateTime + separator);
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        createRemoteDir(dateTime, sftpConfig.getUsername(), sftpConfig.getHost(), sftpConfig.getPort(), sftpConfig.getPassword());
        createRemoteDir(dateTime, sftpConfig.getRemoteUsername(), sftpConfig.getRemoteHost(), sftpConfig.getRemotePort(), sftpConfig.getRemotePassword());
        //生成文件
        String[] fileNames = new String[hnzshDtos.size()];
        for (int i = 0; i < hnzshDtos.size(); i++) {
            List<Hnzsh> list = messageExportMapper.getMessageInfo(dateTime, hnzshDtos.get(i).getCity());
            log.info(list.toString());
            ExcelUtils excelUtils = new ExcelUtils();
            String fileName = dateTime + "_" + hnzshDtos.get(i).getCity() + "_zyds.xlsx";
            fileNames[i] = fileName;
            try {
                SXSSFWorkbook sxssfWorkbook = excelUtils.exportExcel(hnzshDtos.get(i).getMercNm(), list, Hnzsh.class);
                FileOutputStream fos = new FileOutputStream(sftpConfig.getModDir() + dateTime + separator + fileName);
                sxssfWorkbook.write(fos);
                sxssfWorkbook.dispose();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                log.info(e.getMessage());
            }
        }
        //批量上传
        boolean batchUpOrDownloadLocal = isBatchUpOrDownload(path, dateTime, sftpConfig.getUsername(), sftpConfig.getHost(), sftpConfig.getPort(), sftpConfig.getPassword(), fileNames);
        boolean batchUpOrDownloadRemote = isBatchUpOrDownload(path, dateTime, sftpConfig.getRemoteUsername(), sftpConfig.getRemoteHost(), sftpConfig.getRemotePort(), sftpConfig.getRemotePassword(), fileNames);
        //上传之后删除本地文件
        try {
            if (batchUpOrDownloadLocal && batchUpOrDownloadRemote) {
                FileUtils.deleteDirectory(path.toFile());
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    private boolean isBatchUpOrDownload(Path path, String dateTime, String userName, String host, String port, String password, String[] fileNames) {
        return SFTPUtils.batchUpOrDownload(
                userName, host, port, password, sftpConfig.getKeyPath(),
                sftpConfig.getRemotePathUpload() + dateTime + separator,
                sftpConfig.getModDir() + dateTime + separator,
                SFTPUtils.OPERATE_UPLOAD,
                fileNames);
    }

    private void createRemoteDir(String dateTime, String userName, String host, String port, String password) {
        boolean operatesftp = SFTPUtils.operatesftp(userName, host, port, password, sftpConfig.getKeyPath(),
                sftpConfig.getRemotePathUpload() + separator + dateTime,
                null,
                null,
                null,
                SFTPUtils.OPERATE_MKDIR);
        if (!operatesftp) {
            log.info(dateTime + "文件夹已存在,或权限异常");
        }
    }

    @DS("oracle")
    public void getTest() {
        List<Object> test = messageExportMapper.getTest();
        log.info(test.toString());
    }
}
