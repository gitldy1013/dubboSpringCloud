package org.cmcc.service.sftp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.cmcc.service.dto.MercDto;
import org.cmcc.service.sftp.bean.Merc;

public interface MercIService {
    IPage<Merc> selectPageVo(IPage<Merc> page, MercDto state);
}
