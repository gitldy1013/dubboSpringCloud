package org.cmcc.service.hnzsh.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "hbsftp")
public class HbSftpConfig {
    //和包券excel模板路径
    private String hbqTemplateFilePath;
    //和包券生成excel的文件名后缀
    private String hbqFileNameSuffix;
    private String hbqTargetFilePath;

    private String hbhbTemplateFilePath;
    //和包红包生成excel的文件名后缀
    private String hbhbFileNameSuffix;
    private String hbhbTargetFilePath;
    private String host;
    private String port;
    private String username;
    private String password;
    //和包券sftp文件存储路径
    private String hbqFileDir;
    //和包红包sftp文件存储路径
    private String hbhbFileDir;
    private String remoteHost;
    private String remotePort;
    private String remoteUsername;
    private String remotePassword;
    //和包券sftp文件存储路径
    private String hbqRemoteFileDir;
    //和包红包sftp文件存储路径
    private String hbhbRemoteFileDir;


}
