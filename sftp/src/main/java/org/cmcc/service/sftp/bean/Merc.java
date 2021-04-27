package org.cmcc.service.sftp.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@TableName(value = "tm_oil_mkt_stat_d")
public class Merc implements Serializable {

    private String dateCd;
    private String provNm;
    private String provCd;
    private String mercId;
    private String mercNm;
    private String ordDate;
    private int ordNum;
    private double ordAmt;
    private double discountAmt;
    private double oilNum;

}
