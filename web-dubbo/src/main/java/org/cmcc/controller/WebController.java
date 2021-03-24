package org.cmcc.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.cmcc.service.DubboService;
import org.cmcc.service.ExcelExportService;
import org.cmcc.service.dto.ExcelEntityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cmcc
 * @date 2020/6/26
 */
@Controller
@CrossOrigin(origins = "*")
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
    public List<Map<String, Object>> export(String tableName, @RequestParam(value = "colums[]", required = false) String[] colums) {
        return excelExportService.excelExport(tableName, colums);
    }

    @PostMapping("/excel/check")
    @ResponseBody
    public String check(String dir, String username, String host, String port, String pwd) {
        return excelExportService.check(dir, username, host, port, pwd);
    }

    @PostMapping("/excel/upload")
    @ResponseBody
    public String upload(String tableName, @RequestParam(value = "colums[]", required = false) String[] colums,
                         String dir, String username, String host, String port, String pwd) {
        return excelExportService.upload(tableName, colums, dir, username, host, port, pwd);
    }

    @GetMapping("/excel/down")
    public void downExcel(HttpServletResponse response, String fileName, @RequestParam(value = "colums", required = false) String[] colums) throws Exception {
        byte[] data = excelExportService.getFile(fileName, colums);
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xlsx\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
        response.flushBuffer();
    }

    @GetMapping("/excel/list")
    @ResponseBody
    public LinkedHashMap<String, ExcelEntityDto> list(@RequestParam(required = false) String tableName) {
        return excelExportService.showTables(tableName);
    }

    @GetMapping("/excel/tabs")
    @ResponseBody
    public List<String> tabs(@RequestParam(required = false) String tableName) {
        return excelExportService.tableList(tableName);
    }

    @GetMapping({"/", "index", ""})
    public String index() {
        return "index";
    }

    @GetMapping({"/excel/home"})
    public String home() {
        return "home";
    }

    @GetMapping("/excel/index")
    public String excel() {
        return "excel";
    }


}
