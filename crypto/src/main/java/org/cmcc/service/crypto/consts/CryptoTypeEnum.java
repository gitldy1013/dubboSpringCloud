package org.cmcc.service.crypto.consts;


import org.springframework.util.StringUtils;

/**
 * @author zhaolei
 * @date 2021-03-12 16:54
 */

public enum CryptoTypeEnum {
    //响应码20000 是成功
    HMJ_CRYPTO("01","号码借加解密");


    CryptoTypeEnum(String code, String cryptoType) {
        this.code = code;
        this.cryptoType = cryptoType;
    }

    private  String code ;
    private String cryptoType ;

    public static String getCryptoTypeEnum(String code) {
        if (StringUtils.isEmpty(code)) {
            return "";
        }
        for (CryptoTypeEnum cryptoTypeEnum : CryptoTypeEnum.values()) {
            if (cryptoTypeEnum.getCode()==code) {
                return cryptoTypeEnum.getCryptoType();
            }
        }
        return code+"";
    }

    public String getCode() {
        return code;
    }

    public String getCryptoType() {
        return cryptoType;
    }
}
