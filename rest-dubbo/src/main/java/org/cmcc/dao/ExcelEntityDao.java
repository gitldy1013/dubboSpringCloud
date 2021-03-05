package org.cmcc.dao;

import org.cmcc.entity.ExcelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelEntityDao extends JpaRepository<ExcelEntity,Long> {
    ExcelEntity findExcelEntitiesByTableName(String tableName);
}
