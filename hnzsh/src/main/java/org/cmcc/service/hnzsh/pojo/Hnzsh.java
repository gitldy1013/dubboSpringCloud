package org.cmcc.service.hnzsh.pojo;

import org.cmcc.service.hnzsh.annotation.ExcelExportField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static org.cmcc.service.hnzsh.utils.TimeUtils.formatAmt;

@Data
@EqualsAndHashCode(callSuper = false)
public class Hnzsh {
    @ApiModelProperty(value = "日期")
    private String dateCd;
    @ApiModelProperty(value = "地市")
    private String city;
    @ExcelExportField(name = "起始日期", index = 1)
    @ApiModelProperty(value = "起始日期")
    private String startDt;
    @ExcelExportField(name = "截止日期", index = 2)
    @ApiModelProperty(value = "截止日期")
    private String endDt;
    @ExcelExportField(name = "商户编号", index = 3)
    @ApiModelProperty(value = "商户编号")
    private String mercId;
    @ExcelExportField(name = "交易时间", index = 4)
    @ApiModelProperty(value = "交易时间")
    private String trdTm;
    @ExcelExportField(name = "交易账号", index = 5)
    @ApiModelProperty(value = "交易账号")
    private String trdAccNo;
    @ExcelExportField(name = "商户名称", index = 6)
    @ApiModelProperty(value = "商户名称")
    private String mercNm;
    @ExcelExportField(name = "实收金额", index = 7)
    @ApiModelProperty(value = "实收金额")
    private String acrcvAmt;
    @ApiModelProperty(value = "分区")
    private String pDayId;
    public String getAcrcvAmt() {
        return formatAmt(acrcvAmt);
    }
}
