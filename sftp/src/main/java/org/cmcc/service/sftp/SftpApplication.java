package org.cmcc.service.sftp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.cmcc.service.log", "org.cmcc.service.sftp"})
@MapperScan(basePackages = {"org.cmcc.service.sftp.mapper", "org.cmcc.service.log.dao.*"})
@RefreshScope
public class SftpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SftpApplication.class, args);
    }

}

