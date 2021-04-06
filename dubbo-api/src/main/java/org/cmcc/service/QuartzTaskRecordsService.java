package org.cmcc.service;

import org.cmcc.service.bean.QuartzTaskRecords;

import java.util.List;

public interface QuartzTaskRecordsService {

    long addTaskRecords(QuartzTaskRecords quartzTaskRecords);

    Integer updateTaskRecords(QuartzTaskRecords quartzTaskRecords);

    List<QuartzTaskRecords> listTaskRecordsByTaskNo(String taskNo);

}
