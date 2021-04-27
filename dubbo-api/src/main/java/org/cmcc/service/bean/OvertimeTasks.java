package org.cmcc.service.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 告警信息表
 */
@Data
@ToString
public class OvertimeTasks implements Serializable {
    private int id;
    //'调度任务名称'
    private String taskName;
    //'描述'
    private String notes;
    //'插入时间'
    private Date createTime;
}
