package org.cmcc.service;

import org.apache.dubbo.config.annotation.Service;

/**
 * @author cmcc
 * @date 2020/6/26
 */
@Service(protocol = "dubbo")
public class DubboServiceImpl implements DubboService {
    @Override
    public String test(String param) {
        return "dubbo service: " + param;
    }
}
