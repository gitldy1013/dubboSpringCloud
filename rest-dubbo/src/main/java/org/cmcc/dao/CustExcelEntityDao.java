package org.cmcc.dao;

import org.apache.commons.lang.StringUtils;
import org.cmcc.entity.EntitySftpSql;
import org.cmcc.entity.ExcelEntity;
import org.cmcc.service.dto.EntitySftpSqlDto;
import org.cmcc.service.dto.ExcelEntityDto;
import org.cmcc.utils.ExcelEntity2Dto;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustExcelEntityDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EntitySftpSqlDao entitySftpSqlDao;

    @Transactional(readOnly = true)
    public ExcelEntity findTableSql(String tableName) {
        String sql = "show create table " + tableName;
        //创建本地查询
        Query nativeQuery = entityManager.createNativeQuery(sql);
        //用ExcelEntity包装
        for (Object objects : nativeQuery.getResultList()) {
            Object[] object = (Object[]) objects;
            ExcelEntity excelEntity = new ExcelEntity();
            excelEntity.setTableName((String) object[0]);
            excelEntity.setTableSql((String) object[1]);
            return excelEntity;
        }
        return null;
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findByTableNameAndColmsAndSql(String tableName, String sftpSql, Map<String, String> colms) {
        String colmsStr = colms.keySet().toString().replace("[", "").replace("]", "");
        String sql;
        if (StringUtils.isEmpty(sftpSql)) {
            sql = "select " + colmsStr + " from " + tableName;
        } else {
            sql = sftpSql;
        }
        //创建本地查询
        return (List<Map<String, Object>>) entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();

    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> findByTableNameAndColms(String tableName, Map<String, String> colms) {
        return findByTableNameAndColmsAndSql(tableName, null, colms);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, ExcelEntityDto> showTables(String tableName) {
        if (tableName == null) {
            tableName = "";
        }
        String sql = "show tables like '%" + tableName + "%'";
        //创建本地查询
        Query nativeQuery = entityManager.createNativeQuery(sql);
        LinkedHashMap<String, ExcelEntityDto> excelEntitiesMap = new LinkedHashMap<>();
        for (String tName : (List<String>)nativeQuery.getResultList()) {
            EntitySftpSql entitySftpSqlByTableName = entitySftpSqlDao.findEntitySftpSqlByTableName(tName);
            EntitySftpSqlDto entitySftpSqlDto = new EntitySftpSqlDto();
            if (entitySftpSqlByTableName != null) {
                BeanUtils.copyProperties(entitySftpSqlByTableName, entitySftpSqlDto);
            }
            excelEntitiesMap.put(tName, ExcelEntity2Dto.excel2Dto(findTableSql(tName), entitySftpSqlDto));
        }
        return excelEntitiesMap;
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<String> tableList(String tableName) {
        if (tableName == null) {
            tableName = "";
        }
        String sql = "show tables like '%" + tableName + "%'";
        //创建本地查询
        return (List<String>) entityManager.createNativeQuery(sql).getResultList();
    }
}
