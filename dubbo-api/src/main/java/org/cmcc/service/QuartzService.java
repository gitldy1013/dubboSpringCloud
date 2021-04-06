package org.cmcc.service;


import org.cmcc.service.bean.QuartzTaskErrors;
import org.cmcc.service.bean.QuartzTaskInformations;
import org.cmcc.service.bean.QuartzTaskRecords;
import org.cmcc.service.dto.QuartzTaskRecordsVo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface QuartzService {
    String addTask(QuartzTaskInformations quartzTaskInformations);

    String delTask(QuartzTaskInformations quartzTaskInformations);

    List<QuartzTaskInformations> getTaskList(String taskNo, String currentPage);

    QuartzTaskInformations getTaskById(String id);

    String updateTask(QuartzTaskInformations quartzTaskInformations);

    String startOrStopJob(String taskNo);

    void initLoadOnlineTasks();

    void sendMessage(String message) throws ExecutionException, InterruptedException;

    QuartzTaskRecords addTaskRecords(String taskNo);

    Integer updateRecordById(Integer count, Long id);

    Integer updateModifyTimeById(QuartzTaskInformations quartzTaskInformations);

    Integer addTaskErrorRecord(String id, String errorKey, String errorValue);

    List<QuartzTaskRecordsVo> taskRecords(String taskNo);

    String runTaskRightNow(String taskNo);

    QuartzTaskErrors detailTaskErrors(String recordId);
}
