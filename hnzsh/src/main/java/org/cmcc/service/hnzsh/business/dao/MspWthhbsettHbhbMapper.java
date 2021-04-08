package org.cmcc.service.hnzsh.business.dao;

import org.cmcc.service.hnzsh.pojo.HbhbDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MspWthhbsettHbhbMapper {

    List<HbhbDto> getHbhbInfo(String statDt);

}
