package org.cmcc.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.cmcc.service.SftpService;
import org.cmcc.service.dto.EntitySftpSqlDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author cmcc
 * @date 2020/6/26
 */
@Controller
@CrossOrigin(origins = "*")
@Slf4j
public class SftpController {

    @Reference(protocol = "dubbo")
    private SftpService sftpService;

    @GetMapping("/sftp/list")
    @ResponseBody
    public List<EntitySftpSqlDto> list() {
        return sftpService.getList();
    }

    @PostMapping("/sftp/add")
    @ResponseBody
    public String add(EntitySftpSqlDto entitySftpSqlDto) {
        try {
            sftpService.add(entitySftpSqlDto);
            return "操作成功";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "操作异常" + e.getMessage();
        }
    }

    @PostMapping("/sftp/del")
    @ResponseBody
    public String del(EntitySftpSqlDto entitySftpSqlDto) {
        try {
            sftpService.del(entitySftpSqlDto);
            return "操作成功";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "操作异常" + e.getMessage();
        }
    }

    @PostMapping("/sftp/update")
    @ResponseBody
    public String update(EntitySftpSqlDto entitySftpSqlDto) {
        try {
            sftpService.update(entitySftpSqlDto);
            return "操作成功";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "操作异常" + e.getMessage();
        }
    }

    @GetMapping("/sftp/index")
    public String index() {
        return "sftpinfo";
    }


}
