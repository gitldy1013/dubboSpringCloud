package org.cmcc.dao;

import org.cmcc.entity.EntitySftpSql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntitySftpSqlDao extends JpaRepository<EntitySftpSql, String> {
    EntitySftpSql findEntitySftpSqlByTableName(String tableName);
}
