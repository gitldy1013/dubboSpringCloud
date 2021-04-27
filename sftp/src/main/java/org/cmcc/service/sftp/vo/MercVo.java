package org.cmcc.service.sftp.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class MercVo implements Serializable {
    private Integer current;

    private Integer size;

    private Long total;

    private List<org.cmcc.service.sftp.bean.Merc> mercList;
}
