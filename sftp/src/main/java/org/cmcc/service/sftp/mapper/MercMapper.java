package org.cmcc.service.sftp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cmcc.service.dto.MercDto;
import org.cmcc.service.sftp.bean.Merc;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MercMapper extends BaseMapper<Merc> {
    IPage<Merc> selectPageList(IPage<Merc> page,@Param("mercDto") MercDto mercDto);
}
