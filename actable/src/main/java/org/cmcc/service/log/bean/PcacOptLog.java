package org.cmcc.service.log.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cmcc.service.log.annotation.ColumnComment;
import org.cmcc.service.log.annotation.ColumnType;
import org.cmcc.service.log.annotation.IsAutoIncrement;
import org.cmcc.service.log.constants.MySqlTypeConstant;

import java.util.Date;

/**
 * 操作日志表
 *
 * @author cmcc
 * @since 2020-09-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName
public class PcacOptLog {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pcac_opt_log_id", type = IdType.AUTO)
    @IsAutoIncrement
    @ColumnComment("主键")
    @ColumnType(length = 12)
    private Integer pcacOptLogId;

    @TableField
    @ColumnComment("创建者")
    private String createdBy;

    @TableField
    @ColumnComment("操作内容")
    @ColumnType(MySqlTypeConstant.TEXT)
    private String optContent;

    @TableField
    @ColumnComment("创建时间")
    private Date createdTime;

    @TableField
    @ColumnComment("更新者")
    private String updatedBy;

    @TableField
    @ColumnComment("更新时间")
    private Date updatedTime;

}
