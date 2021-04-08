package org.cmcc.quartz.job;

import org.cmcc.quartz.service.quartz.impl.QuartzServiceImpl;
import org.cmcc.service.QuartzService;
import org.cmcc.service.bean.QuartzTaskInformations;
import org.cmcc.service.common.uitl.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName KafkaListener
 * @Description kafka消费者
 * @Author cmcc
 * @Date 2019/1/10
 * Version  1.0
 */
//@Component
public class KafkaListener {

    @Autowired
    private QuartzService quartzService;

    private static final Logger logger = LoggerFactory.getLogger(KafkaListener.class);

    private AtomicInteger atomicInteger;

    @org.springframework.kafka.annotation.KafkaListener(topics = QuartzServiceImpl.QUARTZ_TOPIC)
    public void messageConsumerHandler(String content) {
        logger.info("监听到消息:{}", content);
        atomicInteger = new AtomicInteger(0);
        String id = "";
        String taskNo = "";
        try {
            // message格式 ---------> taskNo:id:executeParameter;
            String[] split = ":".split(content);
            id = split[1];
            taskNo = split[0];
            //TODO kafka逻辑

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            atomicInteger.incrementAndGet();
            quartzService.addTaskErrorRecord(id, taskNo + ":" + ex.getMessage(), CommonUtil.getExceptionDetail(ex));
        }
        quartzService.updateRecordById(atomicInteger.get(), Long.parseLong(id));
        QuartzTaskInformations quartzTaskInformation = new QuartzTaskInformations();
        quartzTaskInformation.setId(Long.parseLong(id));
        quartzTaskInformation.setLastmodifytime(System.currentTimeMillis());
        quartzService.updateTask(quartzTaskInformation);
    }

}
