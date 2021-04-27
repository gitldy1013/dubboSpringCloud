package org.cmcc.service.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MercDto implements Serializable {
    private String provCd;
    private String mercNm;
    private String mercId;
    private String startDate;
    private String endDate;
    private String paginationFlag;
    private String pageNum;
    private String pageSize;
}
