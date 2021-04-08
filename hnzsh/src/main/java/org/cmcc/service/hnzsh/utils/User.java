package org.cmcc.service.hnzsh.utils;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhaolei
 * @date 2021-01-26 17:59
 */
@Data
public class User {
    private String city;
    private Date statisticDate;
    private Date payDate;
    private BigDecimal pay;
    private BigDecimal adjPay;
    private BigDecimal sum;
    private String inf;



}
