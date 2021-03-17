package org.cmcc.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.cmcc.service.DubboService;
import org.cmcc.service.ExcelExportService;
import org.cmcc.service.dto.ExcelEntityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cmcc
 * @date 2020/6/26
 */
@Controller
public class WebController {
    @Autowired
    private DubboService dubboService;

    @Reference(protocol = "dubbo")
    private ExcelExportService excelExportService;

    @GetMapping("/test/{p}")
    @ResponseBody
    public String test(@PathVariable("p") String param) {
        return dubboService.test(param);
    }

    @PostMapping("/excel/export")
    @ResponseBody
    public List<Map<String, Object>> export(String tableName, @RequestParam(value = "colums[]",required = false) String[] colums) {
        return excelExportService.excelExport(tableName, colums);
    }

    @PostMapping("/excel/check")
    @ResponseBody
    public String check(String dir, String username, String host, String port, String pwd) {
        return excelExportService.check(dir, username, host, port, pwd);
    }

    @PostMapping("/excel/upload")
    @ResponseBody
    public String upload(String tableName, @RequestParam(value = "colums[]",required = false) String[] colums,
                         String dir, String username, String host, String port, String pwd) {
        return excelExportService.upload(tableName, colums, dir, username, host, port, pwd);
    }

    @GetMapping("/excel/list")
    @ResponseBody
    public LinkedHashMap<String, ExcelEntityDto> list() {
        return excelExportService.showTables();
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }


}
