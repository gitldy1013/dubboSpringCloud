package org.cmcc.service.crypto.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zhaolei
 * @date 2021-04-23 10:28
 */
@Data
public class CryptoInfo {


    @NotBlank(message = "加解密信息不能为空")
    private String message;
    @NotBlank(message = "加解密类型不能为空")
    private String cryptoType;
}
