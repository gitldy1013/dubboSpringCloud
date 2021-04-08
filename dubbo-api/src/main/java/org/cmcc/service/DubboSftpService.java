package org.cmcc.service;

import org.cmcc.service.bean.SftpReq;
import org.cmcc.service.bean.SftpResp;

public interface DubboSftpService {
    SftpResp getSftpResp(SftpReq sftpReq);
}
