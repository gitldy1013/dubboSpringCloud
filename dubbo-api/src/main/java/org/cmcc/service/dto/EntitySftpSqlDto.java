package org.cmcc.service.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class EntitySftpSqlDto implements Serializable {
    private String tableName;

    private String sftpHost;

    private String sftpPort;

    private String sftpUsername;

    private String sftpPwd;

    private String sftpDir;

    private String cron;

    private String sftpSql;
}
