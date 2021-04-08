package org.cmcc.service;


import org.cmcc.service.bean.QuartzTaskInformations;

import java.util.List;

public interface QuartzTaskInformationsService {
    String insert(QuartzTaskInformations quartzTaskInformations);

    String remove(QuartzTaskInformations quartzTaskInformations);

    List<QuartzTaskInformations> selectList(String taskNo, String currentPage);

    QuartzTaskInformations getTaskById(String id);

    String updateTask(QuartzTaskInformations quartzTaskInformations);

    QuartzTaskInformations getTaskByTaskNo(String taskNo);

    Integer updateStatusById(QuartzTaskInformations quartzTaskInformations);

    List<QuartzTaskInformations> getUnnfrozenTasks(String status);

    Integer updateModifyTimeById(QuartzTaskInformations quartzTaskInformations);

}
