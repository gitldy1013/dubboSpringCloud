package org.cmcc.dao;

import org.cmcc.entity.ExcelEntity;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.query.spi.NativeQueryImplementor;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CustExcelEntityDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<ExcelEntity> findTableSql(String tableName) {
        String sql = "show create table " + tableName;
        //创建本地查询
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = nativeQuery.getResultList();
        ArrayList<ExcelEntity> excelEntities = new ArrayList<>();
        //用ExcelEntity包装
        for (Object[] objects : resultList) {
            ExcelEntity excelEntity = new ExcelEntity();
            excelEntity.setTableName((String) objects[0]);
            excelEntity.setTableSql((String) objects[1]);
            excelEntities.add(excelEntity);
        }
        return excelEntities;
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
}
