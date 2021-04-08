package org.cmcc.service.hnzsh.business.controller;

import org.cmcc.service.hnzsh.business.service.MessageExportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "导出文件")
@Slf4j
public class MessageExportController {

    @Autowired
    private MessageExportService messageExportService;

    @RequestMapping(value = "/messageExport", method = RequestMethod.GET)
    @ApiOperation(value = "信息导出", notes = "湖南中石化定制化对账文件推送到FTP服务器接口")
    public void specialMerchantMsgExport(String dateTime) {
        log.info("==============开始导出==============");
        messageExportService.msgExport(dateTime);
        log.info("==============导出完毕==============");
    }
}
