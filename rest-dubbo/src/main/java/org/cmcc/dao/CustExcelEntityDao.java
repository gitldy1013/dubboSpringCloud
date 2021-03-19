package org.cmcc.dao;

import org.cmcc.entity.ExcelEntity;
import org.cmcc.service.dto.ExcelEntityDto;
import org.cmcc.utils.ExcelEntity2Dto;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.query.spi.NativeQueryImplementor;
import org.hibernate.transform.Transformers;
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

    @Transactional(readOnly = true)
    public ExcelEntity findTableSql(String tableName) {
        String sql = "show create table " + tableName;
        //创建本地查询
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = nativeQuery.getResultList();
        //用ExcelEntity包装
        for (Object[] objects : resultList) {
            ExcelEntity excelEntity = new ExcelEntity();
            excelEntity.setTableName((String) objects[0]);
            excelEntity.setTableSql((String) objects[1]);
            return excelEntity;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> findByTableNameAndColms(String tableName, Map<String, String> colms) {
        String colmsStr = colms.keySet().toString().replace("[", "").replace("]", "");
        String sql = "select " + colmsStr + " from " + tableName;
        //创建本地查询
        Query nativeQuery = entityManager.createNativeQuery(sql);
        NativeQueryImplementor<Map<String, Object>> nativeQueryImplementor = nativeQuery.unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> resultMap = nativeQueryImplementor.getResultList();
        return resultMap;
    }

    public LinkedHashMap<String, ExcelEntityDto> showTables(String tableName) {
        if (tableName == null) {
            tableName = "";
        }
        String sql = "show tables like '%" + tableName + "%'";
        //创建本地查询
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<String> resultList = nativeQuery.getResultList();
        LinkedHashMap<String, ExcelEntityDto> excelEntitiesMap = new LinkedHashMap<>();
        for (String objects : resultList) {
            excelEntitiesMap.put(objects, ExcelEntity2Dto.excel2Dto(findTableSql(objects)));
        }
        return excelEntitiesMap;
    }
}
