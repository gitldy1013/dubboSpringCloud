package org.cmcc.service.sftp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.cmcc.service.DubboSftpService;
import org.cmcc.service.bean.SftpReq;
import org.cmcc.service.bean.SftpResp;
import org.cmcc.service.dto.MercDto;
import org.cmcc.service.sftp.bean.Merc;
import org.cmcc.service.sftp.service.impl.MercServiceImpl;
import org.cmcc.service.sftp.vo.MercVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class SftpController {

    @Autowired
    private DubboSftpService dubboSftpService;

    @Autowired
    private MercServiceImpl mercService;

    @RequestMapping(value = "/hello")
    public String sayHello() {
        return "test hello";
    }

    @RequestMapping(value = "/checkTrans")
    public SftpResp specialMerchantMsgExport(@RequestBody SftpReq sftpReq) {
        return dubboSftpService.getSftpResp(sftpReq);
    }

    @RequestMapping(value = "/mercList")
    public MercVo getMercList(MercDto mercDto) {
        IPage<Merc> page;
        MercVo mercVo = new MercVo();
        if ("Y".equals(mercDto.getPaginationFlag())) {
            page = new Page<Merc>(Long.parseLong(mercDto.getPageNum()), Long.parseLong(mercDto.getPageSize()));
            mercVo.setCurrent(Integer.parseInt(mercDto.getPageNum()));
            mercVo.setSize(Integer.parseInt(mercDto.getPageSize()));
        } else {
            page = new Page<Merc>();
            mercVo.setCurrent(1);
            mercVo.setSize(1);
        }
        IPage<Merc> iPage = mercService.selectPageVo(page, mercDto);
        mercVo.setTotal(iPage.getTotal());
        mercVo.setMercList(iPage.getRecords());
        return mercVo;
    }

}
