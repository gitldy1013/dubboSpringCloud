package org.cmcc.service.hnzsh.runner;

import org.cmcc.service.hnzsh.business.service.MessageExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageExportRunner implements ApplicationRunner {
    @Autowired
    private MessageExportService messageExportService;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        List<String> dateTimes = applicationArguments.getOptionValues("dateTime");
        String dateTime = null;
        if (dateTimes != null && dateTimes.size() > 0) {
            dateTime = dateTimes.get(0);
        }
        messageExportService.msgExport(dateTime);
//        messageExportService.getTest();
    }
}
