package org.cmcc.quartz.service.quartz.impl;


import org.apache.dubbo.config.annotation.Service;
import org.cmcc.service.QuartzTaskRecordsService;
import org.cmcc.service.bean.QuartzTaskRecords;
import org.cmcc.quartz.dao.QuartzTaskRecordsMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName QuartzTaskRecordsServiceImpl
 * @Description TODO
 * @Author cmcc
 * @Date 2019/1/3
 * Version  1.0
 */
@Service(protocol = {"dubbo"})
public class QuartzTaskRecordsServiceImpl implements QuartzTaskRecordsService {

    @Autowired
    private QuartzTaskRecordsMapper quartzTaskRecordsMapper;

    @Override
    public long addTaskRecords(QuartzTaskRecords quartzTaskRecords) {
        return quartzTaskRecordsMapper.insert(quartzTaskRecords);
    }

    @Override
    public Integer updateTaskRecords(QuartzTaskRecords quartzTaskRecords) {
        return quartzTaskRecordsMapper.updateByPrimaryKeySelective(quartzTaskRecords);
    }

    @Override
    public List<QuartzTaskRecords> listTaskRecordsByTaskNo(String taskNo) {
        return quartzTaskRecordsMapper.getTaskRecordsByTaskNo(taskNo);
    }

    ;

}
