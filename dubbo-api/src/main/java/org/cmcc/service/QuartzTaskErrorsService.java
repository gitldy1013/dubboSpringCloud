package org.cmcc.service;

import org.cmcc.service.bean.QuartzTaskErrors;

public interface QuartzTaskErrorsService {
    Integer addTaskErrorRecord(QuartzTaskErrors quartzTaskErrors);

    QuartzTaskErrors detailTaskErrors(String recordId);
}
