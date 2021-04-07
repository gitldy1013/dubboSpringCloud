package org.cmcc.quartz.service.quartz.impl;

import org.cmcc.quartz.dao.QuartzTaskErrorsMapper;
import org.apache.dubbo.config.annotation.Service;
import org.cmcc.service.QuartzTaskErrorsService;
import org.cmcc.service.bean.QuartzTaskErrors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName QuartzTaskErrorsServiceImpl
 * @Description TODO
 * @Author cmcc
 * @Date 2019/1/3
 * Version  1.0
 */
@Service(protocol = {"dubbo"})
public class QuartzTaskErrorsServiceImpl implements QuartzTaskErrorsService {

    @Autowired
    private QuartzTaskErrorsMapper quartzTaskErrorsMapper;

    @Override
    public Integer addTaskErrorRecord(QuartzTaskErrors quartzTaskErrors) {
        return quartzTaskErrorsMapper.insert(quartzTaskErrors);
    }

    @Override
    public QuartzTaskErrors detailTaskErrors(String recordId) {
        return quartzTaskErrorsMapper.detailTaskErrors(recordId);
    }

}
