package org.cmcc.service;

/**
 * @author cmcc
 * @date 2020/6/26
 */
@org.apache.dubbo.config.annotation.DubboService(protocol = "dubbo")
public class DubboServiceImpl implements DubboService {
    @Override
    public String test(String param) {
        return "dubbo service: " + param;
    }
}
