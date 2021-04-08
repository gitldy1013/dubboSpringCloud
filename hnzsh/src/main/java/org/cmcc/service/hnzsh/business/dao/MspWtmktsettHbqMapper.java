package org.cmcc.service.hnzsh.business.dao;

import org.cmcc.service.hnzsh.pojo.HbqDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MspWtmktsettHbqMapper {

    List<HbqDto> getHbqInfo(String statDt);

}
