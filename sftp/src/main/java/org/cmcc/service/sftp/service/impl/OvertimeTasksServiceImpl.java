package org.cmcc.service.sftp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cmcc.service.bean.OvertimeTasks;
import org.cmcc.service.sftp.mapper.OverTimeTasksMapper;
import org.cmcc.service.sftp.service.OvertimeTasksIService;
import org.springframework.stereotype.Service;

@Service
public class OvertimeTasksServiceImpl extends ServiceImpl<OverTimeTasksMapper, OvertimeTasks> implements OvertimeTasksIService {
}
