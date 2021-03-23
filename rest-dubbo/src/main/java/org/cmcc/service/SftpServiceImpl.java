package org.cmcc.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.cmcc.dao.EntitySftpSqlDao;
import org.cmcc.entity.EntitySftpSql;
import org.cmcc.service.dto.EntitySftpSqlDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

@Service(protocol = {"dubbo"})
@Slf4j
public class SftpServiceImpl implements SftpService {

    @Autowired
    private EntitySftpSqlDao entitySftpSqlDao;

    /**
     * 列表
     *
     * @return
     */
    @Override
    public List<EntitySftpSqlDto> getList() {
        List<EntitySftpSql> entitySftpSqlDaoAll = entitySftpSqlDao.findAll();
        List<EntitySftpSqlDto> entitySftpSqlDtos = new LinkedList<>();
        for (int i = 0; i < entitySftpSqlDaoAll.size(); i++) {
            EntitySftpSql entitySftpSql = entitySftpSqlDaoAll.get(i);
            EntitySftpSqlDto entitySftpSqlDto = new EntitySftpSqlDto();
            BeanUtils.copyProperties(entitySftpSql, entitySftpSqlDto);
            entitySftpSqlDtos.add(entitySftpSqlDto);
        }
        return entitySftpSqlDtos;
    }

    /**
     * 新增
     */
    @Override
    public void add(EntitySftpSqlDto entitySftpSqlDto) {
        EntitySftpSql s = new EntitySftpSql();
        BeanUtils.copyProperties(entitySftpSqlDto, s);
        entitySftpSqlDao.save(s);
    }

    /**
     * 删除
     */
    @Override
    public void del(EntitySftpSqlDto entitySftpSqlDto) {
        EntitySftpSql s = new EntitySftpSql();
        BeanUtils.copyProperties(entitySftpSqlDto, s);
        entitySftpSqlDao.delete(s);
    }

    /**
     * 新增
     */
    @Override
    public void update(EntitySftpSqlDto entitySftpSqlDto) {
        EntitySftpSql s = new EntitySftpSql();
        BeanUtils.copyProperties(entitySftpSqlDto, s);
        entitySftpSqlDao.saveAndFlush(s);
    }
}
