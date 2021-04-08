package org.cmcc.service.sftp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sftp")
public class SftpConfig {
    private String host;
    private String port;
    private String username;
    private String password;
    private String modDir;
    private String filePath;
    private String localDir;
}
