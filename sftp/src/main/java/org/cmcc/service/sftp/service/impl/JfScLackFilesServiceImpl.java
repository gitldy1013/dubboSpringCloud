package org.cmcc.service.sftp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cmcc.service.bean.JfscLackFiles;
import org.cmcc.service.sftp.mapper.JfScLackFilesMapper;
import org.cmcc.service.sftp.service.JfScLackFilesIService;
import org.springframework.stereotype.Service;

@Service
public class JfScLackFilesServiceImpl extends ServiceImpl<JfScLackFilesMapper, JfscLackFiles> implements JfScLackFilesIService {
}
