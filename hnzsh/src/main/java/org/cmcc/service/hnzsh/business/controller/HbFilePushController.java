package org.cmcc.service.hnzsh.business.controller;

import org.cmcc.service.hnzsh.business.service.HbFilePushService;
import org.cmcc.service.hnzsh.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author zhaolei
 * @date 2021-01-29 09:59
 */
@Controller
@RequestMapping("/hb")
@Slf4j
public class HbFilePushController {
    @Autowired
    private HbFilePushService hbFilePushService;

    @RequestMapping(value = "/hbqFile",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public void hbqFilePush(String statDt){

        //statDt有值就推送该日期的数据，如果没有值就推送当前日期前一天的数据
        if (StringUtils.isEmpty(statDt)){
            statDt = TimeUtils.getDateBefore(new Date(), -1, "yyyyMMdd");
        }
        log.info("推送文件的日期是：{}",statDt);
        hbFilePushService.HbqFilePush(statDt);
    }

    @RequestMapping(value = "/hbhbFile",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public void hbhbFilePush(String statDt){

        //statDt有值就推送该日期的数据，如果没有值就推送当前日期前一天的数据
        if (StringUtils.isEmpty(statDt)){
            statDt = TimeUtils.getDateBefore(new Date(), -1, "yyyyMMdd");
        }
        log.info("推送文件的日期是：{}",statDt);
        hbFilePushService.HbhbFilePush(statDt);
    }
}
