package org.cmcc.service.hnzsh.config;

import com.alibaba.nacos.api.config.ConfigType;
import lombok.Data;
import org.cmcc.service.hnzsh.pojo.HnzshDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "sftp")
public class SftpConfig {
    private String host;
    private String port;
    private String username;
    private String password;
    private String remoteHost;
    private String remotePort;
    private String remoteUsername;
    private String remotePassword;
    private String keyPath;
    private String modDir;
    private String remotePathUpload;
    private String remotePathDownload;
    private List<HnzshDto> fileNames;

}
