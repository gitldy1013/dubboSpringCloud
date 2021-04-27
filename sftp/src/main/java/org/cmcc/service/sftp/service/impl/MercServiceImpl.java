package org.cmcc.service.sftp.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cmcc.service.dto.MercDto;
import org.cmcc.service.sftp.bean.Merc;
import org.cmcc.service.sftp.mapper.MercMapper;
import org.cmcc.service.sftp.service.MercIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MercServiceImpl extends ServiceImpl<MercMapper, Merc> implements MercIService {
    @Autowired
    private MercMapper mercMapper;

    @Override
    public IPage<Merc> selectPageVo(IPage<Merc> page, MercDto mercDto) {
        return mercMapper.selectPageList(page, mercDto);
    }
}
