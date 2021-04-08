package org.cmcc.service.bean;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SftpReq {
    //发送流水号
    private String reqSeq;
    //发送时间
    private String reqTime;
    //已传送文件总数
    private int transNum;
    //补传文件数
    private int retransNum;
    //补传文件列表
    private List<String> retransFiles;
    //剩余文件数
    private int surplusNum;
    //剩余文件列表
    private List<String> surplusFiles;
    //传输结果
    //1、全部传输完成
    //2、部分传输完成
    private String transResult;
}
