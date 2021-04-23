package org.cmcc.service.crypto.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cmcc.service.common.uitl.Result;
import org.cmcc.service.common.uitl.ResultEnum;
import org.cmcc.service.crypto.consts.CryptoTypeEnum;
import org.cmcc.service.crypto.entity.CryptoInfo;
import org.cmcc.service.crypto.utils.CryptoUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaolei
 * @date 2021-04-23 10:12
 */

@Api(description = "加解密服务管理")
@RestController
@RequestMapping(value = "crypto")
@Slf4j
public class CryptoController {


    @Value("${crypto.hmj.secretKey}")
    private String secretKey;

    @ApiOperation("解密方法")
    @PostMapping(value = "decrypt")
    public Result decrypt(@Validated @RequestBody CryptoInfo cryptoInfo) {
        String decryptData ="";
        String cryptoType = cryptoInfo.getCryptoType();
        switch (cryptoType){
            case "01":
                log.debug("执行了"+CryptoTypeEnum.getCryptoTypeEnum(cryptoType));
                decryptData = CryptoUtils.decrypt(secretKey, cryptoInfo.getMessage());
                break;
             default:return new Result(ResultEnum.DECRYPT_TYPE_FALSE.getCode(),ResultEnum.DECRYPT_TYPE_FALSE.getMessage());
        }


        return new Result(decryptData);
    }

    @ApiOperation("加密方法")
    @PostMapping(value = "encrypt")
    public Result encrypt(@Validated @RequestBody CryptoInfo cryptoInfo) {
        String encryptData ="";
        String cryptoType = cryptoInfo.getCryptoType();
        switch (cryptoType){
            case "01":
                log.debug("执行了"+CryptoTypeEnum.getCryptoTypeEnum(cryptoType));
                encryptData = CryptoUtils.encrypt(secretKey, cryptoInfo.getMessage());
                break;
            default:return new Result(ResultEnum.ENCRYPT_TYPE_FALSE.getCode(),ResultEnum.ENCRYPT_TYPE_FALSE.getMessage());
        }


        return new Result(encryptData);
    }

    public static void main(String[] args) {


        String decryptdata = CryptoUtils.decrypt("45fac8b21c810d7e679d306a71cf1876", "1c96a1f4f4ef47b343e64409b33e9bad");
        System.out.println("解密结果是：" + decryptdata);
        String encryptData = CryptoUtils.encrypt("45fac8b21c810d7e679d306a71cf1876", "13875248956");
        System.out.println("加密结果是：" + encryptData);

    }
}