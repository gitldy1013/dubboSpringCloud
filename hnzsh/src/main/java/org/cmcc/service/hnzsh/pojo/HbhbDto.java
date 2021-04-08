package org.cmcc.service.hnzsh.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhaolei
 * @date 2021-01-28 14:07
 */
@Data
public class HbhbDto {

    private Long rowNum;
    private String provNm="";
    private String statDt="";
    private String paymentDt="";
    private BigDecimal paymentAmt = new BigDecimal("0.00");
    private BigDecimal adjustPaymentAmt=new BigDecimal("0.00");
    private BigDecimal realPaymentAmt =new BigDecimal("0.00");
    private String comeAndGo="";


}
