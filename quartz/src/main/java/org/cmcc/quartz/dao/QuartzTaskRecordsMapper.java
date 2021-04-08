package org.cmcc.quartz.dao;


import org.apache.ibatis.annotations.Mapper;
import org.cmcc.service.bean.QuartzTaskRecords;

import java.util.List;

@Mapper
public interface QuartzTaskRecordsMapper {
    int deleteByPrimaryKey(Long id);

    long insert(QuartzTaskRecords record);

    int insertSelective(QuartzTaskRecords record);

    QuartzTaskRecords selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuartzTaskRecords record);

    int updateByPrimaryKey(QuartzTaskRecords record);

    List<QuartzTaskRecords> getTaskRecordsByTaskNo(String taskNo);

    int deleteByTaskNo(String taskno);
}
