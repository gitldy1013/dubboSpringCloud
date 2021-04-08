package org.cmcc.service.sftp.controller;

import lombok.extern.slf4j.Slf4j;
import org.cmcc.service.DubboSftpService;
import org.cmcc.service.bean.SftpReq;
import org.cmcc.service.bean.SftpResp;
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

    @RequestMapping(value = "/hello")
    public String sayHello() {
        return "test hello";
    }

    @RequestMapping(value = "/checkTrans")
    public SftpResp specialMerchantMsgExport(@RequestBody SftpReq sftpReq) {
        return dubboSftpService.getSftpResp(sftpReq);
    }

}
