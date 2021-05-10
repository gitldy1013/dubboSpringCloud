package org.cmcc.service.model;

import org.cmcc.service.log.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 全部采用actable自有的注解
 */
@Table(comment = "actable简单配置")
public class ACTableSimple {

    @IsKey
    @IsAutoIncrement
    private Long id;

    @Column
    @Index
    @IsNotNull
    private String name;

    @Column
    private Date createTime;

    @Column(defaultValue = "false")
    private Boolean isTrue;

    @Column
    private Integer age;

    @Column
    private BigDecimal price;

    @Column
    @Unique
    private String identitycard;

}
