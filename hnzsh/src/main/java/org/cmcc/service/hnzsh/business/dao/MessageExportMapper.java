package org.cmcc.service.hnzsh.business.dao;

import org.cmcc.service.hnzsh.pojo.Hnzsh;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageExportMapper {
    List<Hnzsh> getMessageInfo(@Param("dateCd") String dateCd, @Param("city") String city);

    List<Object> getTest();
}
