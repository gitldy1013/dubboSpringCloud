package org.cmcc.service.hnzsh.cron;


import org.cmcc.service.hnzsh.business.service.HbFilePushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/** 使用quartz框架 项目中所有的定时任务统一放这里处理 */
@Component
@EnableScheduling
@Async // 启动多线程
@Slf4j
public class ScheduledTask {


  @Autowired
  private HbFilePushService HbFilePushService;

  /** 和包券文件推送 */
  @Scheduled(cron = "0 50 8 ? * *")
  public void submitPcacPersonRiskInfoJob() {
    log.info("每天8:50执行文件推送任务==START==");
    //HbFilePushService.HbTicketFilePush();
    log.info("每天8:50执行文件推送任务==END==");
  }




}
