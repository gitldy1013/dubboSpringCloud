package org.cmcc.quartz.dao;


import org.cmcc.service.bean.QuartzTaskErrors;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuartzTaskErrorsMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByTaskNo(String taskNo);

    int insert(QuartzTaskErrors record);

    int insertSelective(QuartzTaskErrors record);

    QuartzTaskErrors selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuartzTaskErrors record);

    int updateByPrimaryKeyWithBLOBs(QuartzTaskErrors record);

    int updateByPrimaryKey(QuartzTaskErrors record);

    QuartzTaskErrors detailTaskErrors(String recordId);
}
