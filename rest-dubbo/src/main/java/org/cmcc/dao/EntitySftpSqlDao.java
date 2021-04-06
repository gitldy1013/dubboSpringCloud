package org.cmcc.dao;

import org.cmcc.entity.EntitySftpSql;
import org.cmcc.service.dto.EntitySftpSqlDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntitySftpSqlDao extends JpaRepository<EntitySftpSql, String> {
    EntitySftpSql findEntitySftpSqlByTableName(String tableName);
}
