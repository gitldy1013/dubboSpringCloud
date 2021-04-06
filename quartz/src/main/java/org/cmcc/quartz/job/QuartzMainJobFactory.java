package org.cmcc.quartz.job;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.cmcc.quartz.service.quartz.impl.QuartzServiceImpl;
import org.cmcc.quartz.util.ApplicationContextHolder;
import org.cmcc.quartz.util.HttpClientUtil;
import org.cmcc.service.ExcelExportService;
import org.cmcc.service.QuartzService;
import org.cmcc.service.SftpService;
import org.cmcc.service.bean.QuartzTaskInformations;
import org.cmcc.service.bean.QuartzTaskRecords;
import org.cmcc.service.common.uitl.CommonUtil;
import org.cmcc.service.common.uitl.ResultEnum;
import org.cmcc.service.dto.EntitySftpSqlDto;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName QuartzMainJobFactory
 * @Description 定时任务的主要执行逻辑，实现Job接口
 * @Author cmcc
 * @Date 2019/1/7
 * Version  1.0
 */
@DisallowConcurrentExecution
public class QuartzMainJobFactory implements Job {

    private static Logger logger = LoggerFactory.getLogger(QuartzMainJobFactory.class);

    private AtomicInteger atomicInteger;

    @Reference(protocol = "dubbo")
    private SftpService sftpService;

    @Reference(protocol = "dubbo")
    private ExcelExportService excelExportService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        atomicInteger = new AtomicInteger(0);

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String id = jobDataMap.getString("id");
        String taskNo = jobDataMap.getString("taskNo");
        String executorNo = jobDataMap.getString("executorNo");
        String sendType = jobDataMap.getString("sendType");
        String url = jobDataMap.getString("url");
        String executeParameter = jobDataMap.getString("executeParameter");
        logger.info("定时任务被执行:taskNo={},executorNo={},sendType={},url={},executeParameter={}", taskNo, executorNo, sendType, url, executeParameter);
        QuartzService quartzService = (QuartzServiceImpl) ApplicationContextHolder.getBean("quartzServiceImpl");
        QuartzTaskRecords records = null;
        try {
            //保存定时任务的执行记录
            records = quartzService.addTaskRecords(taskNo);
            if (null == records || !ResultEnum.INIT.name().equals(records.getTaskstatus())) {
                logger.info("taskNo={}保存执行记录失败", taskNo);
                return;
            }

            if (ResultEnum.POST.getMessage().equals(sendType)) {
                try {
                    String result = HttpClientUtil.doPost(url, "text/json", executeParameter);
                    logger.info("taskNo={},sendtype={}执行结果result{}", taskNo, sendType, result);
                    if (StringUtils.isEmpty(result)) {
                        throw new RuntimeException("taskNo=" + taskNo + "http方式返回null");
                    }
                } catch (Exception ex) {
                    logger.error("");
                    throw ex;
                }
            } else if (ResultEnum.GET.getMessage().equals(sendType)) {
                try {
                    String result = HttpClientUtil.doGet(url);
                    logger.info("taskNo={},sendtype={}执行结果result{}", taskNo, sendType, result);
                    if (StringUtils.isEmpty(result)) {
                        throw new RuntimeException("taskNo=" + taskNo + "http方式返回null");
                    }
                } catch (Exception ex) {
                    logger.error("");
                    throw ex;
                }
            } else if (ResultEnum.KAFKA.getMessage().equals(sendType)) {
                try {
                    String message = taskNo + ":" + id + ":" + executeParameter;
                    quartzService.sendMessage(message);
                    logger.info("taskNo={},sendtype={}推送至kafka成功", taskNo, sendType);
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                    throw ex;
                }
            } else if (ResultEnum.SFTP.getMessage().equals(sendType)) {
                EntitySftpSqlDto entitySftpSql = sftpService.getEntitySftpSql(taskNo);
                String dir = entitySftpSql.getSftpDir();
                String username = entitySftpSql.getSftpUsername();
                String host = entitySftpSql.getSftpHost();
                String port = entitySftpSql.getSftpPort();
                String pwd = entitySftpSql.getSftpPwd();
                String sql = entitySftpSql.getSftpSql();
                excelExportService.excelExport(taskNo, executeParameter.split(","));
                //上传到sftp文件服务器
                String upload = excelExportService.upload(taskNo, executeParameter.split(","), dir, username, host, port, pwd);
                logger.info("taskNo={},sendtype={} sftp推送任务:{}", taskNo, sendType, upload);
            } else if (ResultEnum.SFTP_TEMP.getMessage().equals(sendType)) {
                EntitySftpSqlDto entitySftpSql = sftpService.getEntitySftpSql(taskNo);
                String dir = entitySftpSql.getSftpDir();
                String username = entitySftpSql.getSftpUsername();
                String host = entitySftpSql.getSftpHost();
                String port = entitySftpSql.getSftpPort();
                String pwd = entitySftpSql.getSftpPwd();
                String sql = entitySftpSql.getSftpSql();
                excelExportService.excelExportCus(taskNo, executeParameter, null);
                //上传到sftp文件服务器
                String upload = excelExportService.upload(new String[]{"fill" + executeParameter}, dir, username, host, port, pwd);
                logger.info("taskNo={},sendtype={} sftp推送任务:{}", taskNo, sendType, upload);
            } else {
                logger.error("未知任务类型");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            atomicInteger.incrementAndGet();
            assert records != null;
            quartzService.addTaskErrorRecord(records.getId().toString(), taskNo + ":" + ex.getMessage(), CommonUtil.getExceptionDetail(ex));
        }

        quartzService.updateRecordById(atomicInteger.get(), records.getId());
        QuartzTaskInformations quartzTaskInformation = new QuartzTaskInformations();
        quartzTaskInformation.setId(Long.parseLong(id));
        quartzTaskInformation.setLastmodifytime(System.currentTimeMillis());
        quartzService.updateTask(quartzTaskInformation);
    }
}
