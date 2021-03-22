package org.cmcc.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@ToString
public class EntitySftpSql {
    @Id
    @Column(nullable = false, unique = true)
    private String tableName;

    @Column(nullable = false, unique = true)
    private String sftpHost;

    @Column
    private String sftpPort;

    @Column
    private String sftpUsername;

    @Column
    private String sftpPwd;

    @Column
    private String sftpDir;

    @Column
    private String sftpSql;
}

