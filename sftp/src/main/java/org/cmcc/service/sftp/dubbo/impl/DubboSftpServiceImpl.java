package org.cmcc.service.sftp.dubbo.impl;

import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import org.cmcc.service.DubboSftpService;
import org.cmcc.service.bean.JfscLackFiles;
import org.cmcc.service.bean.OvertimeTasks;
import org.cmcc.service.bean.SftpReq;
import org.cmcc.service.bean.SftpResp;
import org.cmcc.service.sftp.config.SftpConfig;
import org.cmcc.service.sftp.exception.CustomerMsgException;
import org.cmcc.service.sftp.service.impl.JfScLackFilesServiceImpl;
import org.cmcc.service.sftp.service.impl.OvertimeTasksServiceImpl;
import org.cmcc.service.sftp.utils.SFTPUtils;
import org.cmcc.service.sftp.utils.TxtFileUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import static org.cmcc.service.sftp.utils.TimeUtils.getDateBefore;

@org.apache.dubbo.config.annotation.DubboService(protocol = "dubbo")
@Slf4j
public class DubboSftpServiceImpl implements DubboSftpService {
    @Autowired
    private JfScLackFilesServiceImpl jfScLackFilesIService;
    @Autowired
    private OvertimeTasksServiceImpl overtimeTasksIService;

    @Override
    public SftpResp getSftpResp(SftpReq sftpReq) {
        //请求数据写入文件
        SftpConfig sftpConfig = SFTPUtils.getSftpConfig();
        List<String> sftpReqStrs = new ArrayList<>();
        sftpReqStrs.add(sftpReq.toString());
        TxtFileUtil.writeFileContext(sftpReqStrs, sftpConfig.getLocalDir(), "Reqlog.txt");
        SftpResp sftpResp = new SftpResp();
        sftpResp.setReqSeq(sftpReq.getReqSeq());
        sftpResp.setReqTime(sftpReq.getReqTime());
        //扫描文件服务器
        List<String> list = TxtFileUtil.getFileContext(sftpConfig.getFilePath());
        Vector<String> lacks = new Vector<>();
        Vector<ChannelSftp.LsEntry> vector;
        try {
            vector = SFTPUtils.checkSftp();
            List<String> fileNames = new ArrayList<>();
            for (ChannelSftp.LsEntry lsEntry : vector) {
                fileNames.add(lsEntry.getFilename().trim());
            }
            log.info("文件服务器已存在的文件列表：" + fileNames);
            List<JfscLackFiles> jfscLackFiles = new ArrayList<>();
            for (String s : list) {
                String lack = s.replace("YYYYMMDD", getDateBefore(new Date(), -1));
                log.info("当前文件：" + lack);
                boolean contains = fileNames.contains(lack.trim());
                if (!contains) {
                    lacks.add(lack.trim());
                    JfscLackFiles jfscEntity = new JfscLackFiles();
                    jfscEntity.setReqSeq(sftpReq.getReqSeq());
                    jfscEntity.setCheckTime(new Date());
                    jfscEntity.setCreatTime(new Date());
                    jfscEntity.setLackFileName(lack.trim());
                    jfscLackFiles.add(jfscEntity);
                }
            }
            if (list.size() == fileNames.size()) {
                sftpResp.setResultMsg("接收成功，文件数完整");
            } else {
                try {
                    jfScLackFilesIService.saveBatch(jfscLackFiles);
                    OvertimeTasks overtimeTasks = new OvertimeTasks();
                    overtimeTasks.setCreateTime(new Date());
                    overtimeTasks.setTaskName("积分商城文件数校验");
                    overtimeTasks.setNotes(getDateBefore(new Date(), -1) + "账期，积分商城文件同步校验，当前缺失文件数：" + lacks.size() + "，请及时关注！");
                    overtimeTasksIService.save(overtimeTasks);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                sftpResp.setResultMsg("接收成功，实际应该获取" + list.size() + "个文件，存在" + lacks.size() + "个文件未收到");
            }
            sftpResp.setResultCode("0");
            sftpResp.setTransNum(list.size());
            sftpResp.setLackNum(lacks.size());
            sftpResp.setLackFiles(lacks);
        } catch (CustomerMsgException e) {
            log.error(e.getRespDesc());
            sftpResp.setResultMsg(e.getRespDesc());
            sftpResp.setResultCode(e.getRespCode());
        }
        List<String> sftpRespStrs = new ArrayList<>();
        sftpRespStrs.add(sftpResp.toString());
        TxtFileUtil.writeFileContext(sftpRespStrs, sftpConfig.getLocalDir(), "Reqlog.txt");
        return sftpResp;
    }
}
