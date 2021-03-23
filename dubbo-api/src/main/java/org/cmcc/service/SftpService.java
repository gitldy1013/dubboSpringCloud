package org.cmcc.service;

import org.cmcc.service.dto.EntitySftpSqlDto;

import java.util.List;

public interface SftpService {

    public List<EntitySftpSqlDto> getList();
    public void add(EntitySftpSqlDto entitySftpSqlDto);
    public void del(EntitySftpSqlDto entitySftpSqlDto);
    public void update(EntitySftpSqlDto entitySftpSqlDto);
}
