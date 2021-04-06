package org.cmcc.quartz.service.quartz.impl;


import org.cmcc.quartz.job.QuartzMainJobFactory;
import org.apache.dubbo.config.annotation.Service;
import org.cmcc.service.ExcelExportService;
import org.cmcc.service.common.uitl.CommonUtil;
import org.cmcc.quartz.util.HttpClientUtil;
import org.cmcc.service.common.uitl.ResultEnum;
import org.cmcc.service.common.uitl.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.cmcc.service.QuartzService;
import org.cmcc.service.QuartzTaskErrorsService;
import org.cmcc.service.QuartzTaskInformationsService;
import org.cmcc.service.QuartzTaskRecordsService;
import org.cmcc.service.SftpService;
import org.cmcc.service.bean.QuartzTaskErrors;
import org.cmcc.service.bean.QuartzTaskInformations;
import org.cmcc.service.bean.QuartzTaskRecords;
import org.cmcc.service.dto.EntitySftpSqlDto;
import org.cmcc.service.dto.QuartzTaskRecordsVo;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName QuartzServiceImpl
 * @Description quartz service逻辑相关
 * @Author cmcc
 * @Date 2019/1/3
 * Version  1.0
 */
@Service(protocol = {"dubbo"})
@Slf4j
public class QuartzServiceImpl implements QuartzService, InitializingBean {

    public static final String QUARTZ_TOPIC = "quartztopic";
    private static final Logger logger = LoggerFactory.getLogger(QuartzServiceImpl.class);
    private AtomicInteger atomicInteger;

    @Autowired
    @SuppressWarnings("all")
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private QuartzTaskInformationsService quartzTaskInformationsService;

    @Autowired
    private QuartzTaskErrorsService quartzTaskErrorsService;

    @Autowired
    private QuartzTaskRecordsService quartzTaskRecordsService;

    @Autowired
    private SchedulerFactoryBean schedulerBean;

    @Reference(protocol = "dubbo")
    private SftpService sftpService;

    @Reference(protocol = "dubbo")
    private ExcelExportService excelExportService;

    /**
     * 列表查询所有定时任务
     *
     * @return
     */
    @Override
    public List<QuartzTaskInformations> getTaskList(String taskNo, String currentPage) {
        return quartzTaskInformationsService.selectList(taskNo, currentPage);
    }

    /**
     * 新增定时任务
     *
     * @param quartzTaskInformations
     * @return
     */
    @Override
    public String addTask(QuartzTaskInformations quartzTaskInformations) {
        return quartzTaskInformationsService.insert(quartzTaskInformations);
    }

    /**
     * 删除定时任务
     *
     * @param quartzTaskInformations
     * @return
     */
    @Override
    public String delTask(QuartzTaskInformations quartzTaskInformations) {
        QuartzTaskInformations taskByTaskNo = quartzTaskInformationsService.getTaskByTaskNo(quartzTaskInformations.getTaskno());
        if (taskByTaskNo.getFrozenstatus().equals(ResultEnum.UNFROZEN.getMessage())) {
            startOrStopJob(quartzTaskInformations.getTaskno());
        }
        quartzTaskErrorsService.delTaskErrorRecord(quartzTaskInformations.getTaskno());
        quartzTaskRecordsService.delTaskRecords(quartzTaskInformations.getTaskno());
        return quartzTaskInformationsService.remove(quartzTaskInformations);
    }

    @Override
    public QuartzTaskInformations getTaskById(String id) {
        return quartzTaskInformationsService.getTaskById(id);
    }

    @Override
    public String updateTask(QuartzTaskInformations quartzTaskInformations) {
        return quartzTaskInformationsService.updateTask(quartzTaskInformations);
    }

    /**
     * 启动 or 暂停定时任务
     *
     * @param taskNo
     * @return
     * @throws SchedulerException
     */
    @Override
    @Transactional
    public String startOrStopJob(String taskNo) {
        QuartzTaskInformations quartzTaskInformation = quartzTaskInformationsService.getTaskByTaskNo(taskNo);
        if (quartzTaskInformation == null) {
            return ResultUtil.success(ResultEnum.NO_DATA.getCode(), ResultEnum.NO_DATA.getMessage());
        }
        String status = quartzTaskInformation.getFrozenstatus();
        Scheduler scheduler = schedulerBean.getScheduler();
        long currentTimeMillis = System.currentTimeMillis();
        QuartzTaskInformations task = new QuartzTaskInformations();
        task.setId(quartzTaskInformation.getId());
        task.setVersion(quartzTaskInformation.getVersion());
        //说明要暂停
        try {
            if (ResultEnum.UNFROZEN.name().equals(status)) {
                scheduler.deleteJob(new JobKey(taskNo));
                task.setFrozentime(currentTimeMillis);
                task.setFrozenstatus(ResultEnum.FROZEN.name());
                //说明要启动
            } else if (ResultEnum.FROZEN.name().equals(status)) {
                scheduler.deleteJob(new JobKey(taskNo));
                this.schedule(quartzTaskInformation, scheduler);
                task.setUnfrozentime(currentTimeMillis);
                task.setFrozenstatus(ResultEnum.UNFROZEN.name());
            }
        } catch (Exception e) {
            log.error("/list/optionjob exception={}", e.getMessage());
            return ResultUtil.fail();
        }
        task.setLastmodifytime(currentTimeMillis);
        quartzTaskInformationsService.updateStatusById(task);
        logger.info("taskNo={},taskName={},scheduleRule={},任务{}成功", quartzTaskInformation.getTaskno(), quartzTaskInformation.getTaskname(), quartzTaskInformation.getSchedulerrule(), ResultEnum.FROZEN.name().equals(status) ? "启动" : "暂停");
        return ResultUtil.success(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 初始化加载定时任务
     *
     * @throws Exception
     */
    @Override
    public void initLoadOnlineTasks() {
        List<QuartzTaskInformations> unnfrozenTasks = quartzTaskInformationsService.getUnnfrozenTasks(ResultEnum.UNFROZEN.name());
        if (CollectionUtils.isEmpty(unnfrozenTasks)) {
            logger.info("没有需要初始化加载的定时任务");
            return;
        }
        Scheduler scheduler = schedulerBean.getScheduler();
        for (QuartzTaskInformations unnfrozenTask : unnfrozenTasks) {
            try {
                this.schedule(unnfrozenTask, scheduler);
            } catch (Exception e) {
                logger.error("系统初始化加载定时任务:taskno={},taskname={}失败原因exception={}", unnfrozenTask.getTaskno(), unnfrozenTask.getTaskname(), e);
            }
        }
    }

    /**
     * 初始化加载定时任务
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.initLoadOnlineTasks();
    }

    @Override
    public QuartzTaskRecords addTaskRecords(String taskNo) {
        QuartzTaskRecords quartzTaskRecords = null;
        try {
            QuartzTaskInformations quartzTaskInformation = quartzTaskInformationsService.getTaskByTaskNo(taskNo);
            if (null == quartzTaskInformation || ResultEnum.FROZEN.name().equals(quartzTaskInformation.getFrozenstatus())) {
                logger.info("taskNo={} not exist or status is frozen!");
                return null;
            }
            long currentTimeMillis = System.currentTimeMillis();
            QuartzTaskInformations task = new QuartzTaskInformations();
            task.setId(quartzTaskInformation.getId());
            task.setLastmodifytime(currentTimeMillis);
            quartzTaskInformationsService.updateTask(task);
            logger.info("taskNo={},taskName={}更新最后修改时间成功", quartzTaskInformation.getTaskno(), quartzTaskInformation.getTaskname());

            quartzTaskRecords = new QuartzTaskRecords();
            quartzTaskRecords.setTaskno(taskNo);
            quartzTaskRecords.setTimekeyvalue(quartzTaskInformation.getTimekey());
            quartzTaskRecords.setExecutetime(currentTimeMillis);
            quartzTaskRecords.setTaskstatus(ResultEnum.INIT.name());
            quartzTaskRecords.setFailcount(0);
            quartzTaskRecords.setFailreason("");
            quartzTaskRecords.setCreatetime(currentTimeMillis);
            quartzTaskRecords.setLastmodifytime(currentTimeMillis);
            quartzTaskRecordsService.addTaskRecords(quartzTaskRecords);
            logger.info("taskNo={},taskName={}添加执行记录表成功", quartzTaskInformation.getTaskno(), quartzTaskInformation.getTaskname());
        } catch (Exception ex) {
            logger.error("添加执行记录表异常exceptio={}", ex);
            return null;
        }
        return quartzTaskRecords;
    }

    @Override
    public Integer updateRecordById(Integer count, Long id) {
        QuartzTaskRecords records = new QuartzTaskRecords();
        records.setId(id);
        records.setFailcount(count);
        records.setLastmodifytime(System.currentTimeMillis());
        if (count > 0) {
            records.setTaskstatus(ResultEnum.FAIL.name());
        } else {
            records.setTaskstatus(ResultEnum.SUCCESS.name());
        }
        return quartzTaskRecordsService.updateTaskRecords(records);
    }

    @Override
    public Integer updateModifyTimeById(QuartzTaskInformations quartzTaskInformations) {
        return quartzTaskInformationsService.updateModifyTimeById(quartzTaskInformations);
    }

    @Override
    public Integer addTaskErrorRecord(String id, String errorKey, String errorValue) {
        QuartzTaskErrors taskErrors = new QuartzTaskErrors();
        taskErrors.setTaskexecuterecordid(String.valueOf(id));
        taskErrors.setErrorkey(errorKey);
        taskErrors.setCreatetime(System.currentTimeMillis());
        taskErrors.setLastmodifytime(System.currentTimeMillis());
        taskErrors.setErrorvalue(errorValue);
        return quartzTaskErrorsService.addTaskErrorRecord(taskErrors);
    }

    public void schedule(QuartzTaskInformations quartzTaskInfo, Scheduler scheduler) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(quartzTaskInfo.getTaskno(), Scheduler.DEFAULT_GROUP);
        JobDetail jobDetail = JobBuilder.newJob(QuartzMainJobFactory.class).withDescription(quartzTaskInfo.getTaskname()).withIdentity(quartzTaskInfo.getTaskno(), Scheduler.DEFAULT_GROUP).build();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("id", quartzTaskInfo.getId().toString());
        jobDataMap.put("taskNo", quartzTaskInfo.getTaskno());
        jobDataMap.put("executorNo", quartzTaskInfo.getExecutorno());
        jobDataMap.put("sendType", quartzTaskInfo.getSendtype());
        jobDataMap.put("url", quartzTaskInfo.getUrl());
        jobDataMap.put("executeParameter", quartzTaskInfo.getExecuteparamter());
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartzTaskInfo.getSchedulerrule());
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withDescription(quartzTaskInfo.getTaskname()).withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
        logger.info("taskNo={},taskName={},scheduleRule={} load to quartz success!", quartzTaskInfo.getTaskno(), quartzTaskInfo.getTaskname(), quartzTaskInfo.getSchedulerrule());
    }

    /**
     * kafka推送消息
     *
     * @param message
     */
    @Override
    public void sendMessage(String message) {
        kafkaTemplate.send(QUARTZ_TOPIC, message);
    }

    @Override
    public List<QuartzTaskRecordsVo> taskRecords(String taskNo) {
        List<QuartzTaskRecords> quartzTaskRecords = quartzTaskRecordsService.listTaskRecordsByTaskNo(taskNo);
        QuartzTaskRecordsVo recordsVo = null;
        List<QuartzTaskRecordsVo> voList = new ArrayList<>();
        for (QuartzTaskRecords quartzTaskRecord : quartzTaskRecords) {
            recordsVo = new QuartzTaskRecordsVo();
            BeanUtils.copyProperties(quartzTaskRecord, recordsVo);
            recordsVo.setTime(quartzTaskRecord.getLastmodifytime() - quartzTaskRecord.getCreatetime());
            voList.add(recordsVo);
        }
        return voList;
    }

    /**
     * 立即运行一次定时任务
     *
     * @param taskNo
     * @return
     */
    @Override
    public String runTaskRightNow(String taskNo) {
        atomicInteger = new AtomicInteger(0);
        QuartzTaskInformations quartzTaskInformations = quartzTaskInformationsService.getTaskByTaskNo(taskNo);
        if (quartzTaskInformations == null) {
            return ResultUtil.success(ResultEnum.NO_DATA.getCode(), ResultEnum.NO_DATA.getMessage());
        }
        Long id = quartzTaskInformations.getId();
        String sendType = quartzTaskInformations.getSendtype();
        String executorNo = quartzTaskInformations.getExecutorno();
        String url = quartzTaskInformations.getUrl();
        String executeParameter = quartzTaskInformations.getExecuteparamter();
        logger.info("定时任务被执行:taskNo={},executorNo={},sendType={},url={},executeParameter={}", taskNo, executorNo, sendType, url, executeParameter);
        QuartzTaskRecords records = null;
        try {
            //保存定时任务的执行记录
            records = this.addTaskRecords(taskNo);
            if (null == records || !ResultEnum.INIT.name().equals(records.getTaskstatus())) {
                logger.info("taskNo={}立即运行失--->>保存执行记录失败", taskNo);
                return ResultUtil.success(ResultEnum.RUN_NOW_FAIL.getCode(), ResultEnum.RUN_NOW_FAIL.getMessage());
            }

            if (ResultEnum.POST.getMessage().equals(sendType)) {
                try {
                    HttpClientUtil.doPost(url, "text/json", executeParameter);
                    logger.info("请求" + url + "成功！");
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                    atomicInteger.incrementAndGet();
                    throw ex;
                }
            } else if (ResultEnum.GET.getMessage().equals(sendType)) {
                try {
                    String s = HttpClientUtil.doGet(url);
                    logger.info("请求" + url + "成功！");
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                    atomicInteger.incrementAndGet();
                    throw ex;
                }
            } else if (ResultEnum.KAFKA.getMessage().equals(sendType)) {
                try {
                    String message = new StringBuffer(taskNo).append(":").append(executeParameter).toString();
                    this.sendMessage(message);
                    logger.info("给kafka推送消息message={}成功", message);
                } catch (Exception ex) {
                    logger.error("给kafka推送消息message={}失败", ex.getMessage());
                    atomicInteger.incrementAndGet();
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
            logger.error("");
            atomicInteger.incrementAndGet();
            this.addTaskErrorRecord(records.getId().toString(), taskNo + ":" + ex.getMessage(), CommonUtil.getExceptionDetail(ex));
        }
        //更改record表的执行状态、最后修改时间、失败个数
        this.updateRecordById(atomicInteger.get(), records.getId());

        //更新taskinfo表的最后修改时间
        QuartzTaskInformations quartzTaskInformation = new QuartzTaskInformations();
        quartzTaskInformation.setId(id);
        quartzTaskInformation.setLastmodifytime(System.currentTimeMillis());
        this.updateTask(quartzTaskInformation);
        return ResultUtil.success(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
    }

    @Override
    public QuartzTaskErrors detailTaskErrors(String recordId) {
        return quartzTaskErrorsService.detailTaskErrors(recordId);
    }


}
