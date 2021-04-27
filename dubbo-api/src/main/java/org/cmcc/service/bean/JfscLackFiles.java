package org.cmcc.service.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class JfscLackFiles implements Serializable {
    private int checkId;
    private String reqSeq;
    private String lackFileName;
    private Date checkTime;
    private Date creatTime;
}
