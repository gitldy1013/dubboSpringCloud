package org.cmcc.service.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class SftpResp implements Serializable {
    //请求的流水号，与请求报文内一致
    private String reqSeq;
    //发送时间
    private String reqTime;
    //返回码
    //0接收成功，文件数完整
    //1接收成功，文件数缺失
    //2接口失败，接口异常
    private String resultCode;
    //返回信息，失败时返回具体失败原因
    private String resultMsg;
    //已收到文件数
    private int transNum;
    //缺失文件数
    private int lackNum;
    //缺失文件列表
    private List<String> lackFiles;

}
