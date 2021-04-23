package org.cmcc.service.crypto.entity.valid;

import lombok.Data;

/**
 * @author zhaolei
 * @date 2021-04-15 17:08
 */
@Data
public class InvalidArgumentInfo {
    private String field;
    private Object rejectedValue;
    private String defaultMessage;
}
