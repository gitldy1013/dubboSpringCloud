package org.cmcc.service.hnzsh.business.service.Impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.cmcc.service.hnzsh.business.dao.MspWthhbsettHbhbMapper;
import org.cmcc.service.hnzsh.business.dao.MspWtmktsettHbqMapper;
import org.cmcc.service.hnzsh.business.service.HbFilePushService;
import org.cmcc.service.hnzsh.config.HbSftpConfig;
import org.cmcc.service.hnzsh.pojo.HbhbDto;
import org.cmcc.service.hnzsh.pojo.HbqDto;
import org.cmcc.service.hnzsh.utils.CustomizeExcelUtils;
import org.cmcc.service.hnzsh.utils.SFTPUtils;
import org.cmcc.service.hnzsh.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * @author zhaolei
 * @date 2021-01-27 15:23
 */
@Service
@Slf4j
public class HbFilePushServiceImpl implements HbFilePushService {

    private String separator = File.separator;
      //  private String separator = "/";
        @Autowired
        private MspWtmktsettHbqMapper mspWtmktsettHbqMapper;
        @Autowired
        private HbSftpConfig hbSftpConfig;
        @Autowired
        private MspWthhbsettHbhbMapper mspWthhbsettHbhbMapper;

    @Override
    @DS("oracle")
    public void HbqFilePush(String statDt) {
        try {


        String[] arr = {"rowNum","provNm", "statDt","paymentDt", "paymentAmt", "adjustPaymentAmt", "realPaymentAmt", "comeAndGo"};
        //String templateFilePath = "E:\\和包券委托付款结算单template.xlsx";
        String templateFilePath=hbSftpConfig.getHbqTemplateFilePath();
        String fileName = TimeUtils.getPatternDateStr(statDt,"yyyyMMdd","yyyy年MM月dd日") + hbSftpConfig.getHbqFileNameSuffix();
        String targetFile = hbSftpConfig.getHbqTargetFilePath() +fileName;

        String username = hbSftpConfig.getUsername();
        String password = hbSftpConfig.getPassword();
        String host = hbSftpConfig.getHost();
        String port = hbSftpConfig.getPort();
        String filePath = hbSftpConfig.getHbqFileDir()+statDt.substring(0,6)+separator;

        String hbqDeleteFilePath = hbSftpConfig.getHbqTargetFilePath() + TimeUtils.getDateBefore(new Date(), -2, "yyyy年MM月dd日") + hbSftpConfig.getHbqFileNameSuffix();

        //78服务器的sftp参数
            String remoteUsername = hbSftpConfig.getRemoteUsername();
            String remotePassword = hbSftpConfig.getRemotePassword();
            String remoteHost = hbSftpConfig.getRemoteHost();
            String remotePort = hbSftpConfig.getRemotePort();
            String remoteFileDir = hbSftpConfig.getHbqRemoteFileDir()+statDt.substring(0,6)+separator;

            List<HbqDto> hbqInfos = mspWtmktsettHbqMapper.getHbqInfo(statDt);
        log.info("查询"+statDt+"和包券数据结果是：{}",hbqInfos);
        //生成excel文件
        CustomizeExcelUtils.exportExcel(templateFilePath,targetFile,hbqInfos,HbqDto.class,arr,statDt);
        //sftp推送文件到备份服务器
        boolean isDir = SFTPUtils.operatesftp(username, host, port, password, null, filePath, null, null, null, SFTPUtils.OPERATE_MKDIR);
        if (isDir){
           log.info("创建71服务器目录成功");
        }else {
            log.info("71服务器目录已经存在！！");
        }
        boolean fileSuccess = SFTPUtils.operatesftp(username, host, port, password, null, filePath, fileName, hbSftpConfig.getHbqTargetFilePath(), fileName, SFTPUtils.OPERATE_UPLOAD);

        boolean isRemoteDir = SFTPUtils.operatesftp(remoteUsername, remoteHost, remotePort, remotePassword, null, remoteFileDir, null, null, null, SFTPUtils.OPERATE_MKDIR);
            if (isRemoteDir){
                log.info("创建远程服务器目录成功");
            }else {
                log.info("远程服务器目录已经存在！！");
            }
            boolean remoteFileSuccess = SFTPUtils.operatesftp(remoteUsername, remoteHost, remotePort, remotePassword, null, remoteFileDir, fileName, hbSftpConfig.getHbqTargetFilePath(), fileName, SFTPUtils.OPERATE_UPLOAD);
            if (fileSuccess&&remoteFileSuccess){

                deleteBeforeFile(hbqDeleteFilePath);
            }
        }catch (Exception e){
            log.error("=====推送和包券文件失败====={}",e.getMessage());
        }
    }

    @Override
    @DS("oracle")
    public void HbhbFilePush(String statDt) {
        try {


            String[] arr = {"rowNum","provNm", "statDt", "paymentDt", "paymentAmt", "adjustPaymentAmt", "realPaymentAmt", "comeAndGo"};
            //String templateFilePath = "E:\\和包红包委托付款结算单template.xlsx";
            String templateFilePath=hbSftpConfig.getHbhbTemplateFilePath();
            String fileName = TimeUtils.getPatternDateStr(statDt,"yyyyMMdd","yyyy年MM月dd日") + hbSftpConfig.getHbhbFileNameSuffix();
            String targetFile = hbSftpConfig.getHbhbTargetFilePath() +fileName;

            String username = hbSftpConfig.getUsername();
            String password = hbSftpConfig.getPassword();
            String host = hbSftpConfig.getHost();
            String port = hbSftpConfig.getPort();
            String filePath = hbSftpConfig.getHbhbFileDir()+statDt.substring(0,6)+separator;

            String hbhbDeleteFilePath = hbSftpConfig.getHbhbTargetFilePath() + TimeUtils.getDateBefore(new Date(), -2, "yyyy年MM月dd日") + hbSftpConfig.getHbhbFileNameSuffix();

            //78服务器的sftp参数
            String remoteUsername = hbSftpConfig.getRemoteUsername();
            String remotePassword = hbSftpConfig.getRemotePassword();
            String remoteHost = hbSftpConfig.getRemoteHost();
            String remotePort = hbSftpConfig.getRemotePort();
            String remoteFilePath = hbSftpConfig.getHbhbRemoteFileDir()+statDt.substring(0,6)+separator;

            List<HbhbDto> hbhbInfos = mspWthhbsettHbhbMapper.getHbhbInfo(statDt);
            log.info("查询"+statDt+"和包红包数据结果是：{}",hbhbInfos);
            //生成excel文件
            CustomizeExcelUtils.exportExcel(templateFilePath,targetFile,hbhbInfos,HbhbDto.class,arr,statDt);
            //sftp推送文件到备份服务器
            boolean isDir = SFTPUtils.operatesftp(username, host, port, password, null, filePath, null, null, null, SFTPUtils.OPERATE_MKDIR);
            if (isDir){
                log.info("创建71服务器目录成功");
            }else {
                log.info("71服务器目录已经存在！！");
            }
            boolean fileSuccess = SFTPUtils.operatesftp(username, host, port, password, null, filePath, fileName, hbSftpConfig.getHbhbTargetFilePath(), fileName, SFTPUtils.OPERATE_UPLOAD);

            boolean isRemoteDir = SFTPUtils.operatesftp(remoteUsername, remoteHost, remotePort, remotePassword, null, remoteFilePath, null, null, null, SFTPUtils.OPERATE_MKDIR);
            if (isRemoteDir){
                log.info("创建远程服务器目录成功");
            }else {
                log.info("远程服务器目录已经存在！！");
            }
            boolean remoteFileSuccess = SFTPUtils.operatesftp(remoteUsername, remoteHost, remotePort, remotePassword, null, remoteFilePath, fileName, hbSftpConfig.getHbhbTargetFilePath(), fileName, SFTPUtils.OPERATE_UPLOAD);
            if (fileSuccess&&remoteFileSuccess){

                deleteBeforeFile(hbhbDeleteFilePath);
            }
        }catch (Exception e){
            log.error("=====推送和包红包文件失败====={}",e.getMessage());
        }
    }

    void deleteBeforeFile(String deleteFilePath){
            File yesterdayFile = new File(deleteFilePath);
            if (yesterdayFile.exists()){
                boolean delete = yesterdayFile.delete();
                if (delete){
                    log.info("昨天的和包红包文件删除成功！=={}",deleteFilePath);
                }
            }
    }


}
