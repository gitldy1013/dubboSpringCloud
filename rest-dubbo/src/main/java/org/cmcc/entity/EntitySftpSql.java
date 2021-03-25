package org.cmcc.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@ToString
@Proxy(lazy = false)
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

