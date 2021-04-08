package org.cmcc.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@ToString
public class ExcelEntity {
    //Id注解表明该变量对应与数据库
    @Id
    //GeneratedValue注解设置id为自增长
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tableName;

    @Column
    private String tableSql;
}

