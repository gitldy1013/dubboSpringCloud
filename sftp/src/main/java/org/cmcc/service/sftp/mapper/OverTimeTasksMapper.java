package org.cmcc.service.sftp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.cmcc.service.bean.OvertimeTasks;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OverTimeTasksMapper extends BaseMapper<OvertimeTasks> {


}
