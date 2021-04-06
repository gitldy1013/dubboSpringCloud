package org.cmcc.service;

import org.cmcc.service.bean.QuartzTaskErrors;

public interface QuartzTaskErrorsService {
    Integer delTaskErrorRecord(String taskNo);

    Integer addTaskErrorRecord(QuartzTaskErrors quartzTaskErrors);

    QuartzTaskErrors detailTaskErrors(String recordId);
}
