package org.cmcc.service.sftp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.cmcc.service.bean.JfscLackFiles;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface JfScLackFilesMapper extends BaseMapper<JfscLackFiles> {


}
