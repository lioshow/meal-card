package com.lx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 刘肖
 * @since 2023-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Student对象", description="")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "stuNo")
    private Integer stuNo;

    private String password;

    @TableField("stuName")
    private String stuName;

    private String gender;

    @TableField("classInfo")
    private String classInfo;

    // 设置为非数据库字段
    @TableField(exist = false)
    private Integer cardNo;

    @TableField(exist = false)
    private Integer balance;

    @TableField(exist = false)
    private String state;
}
