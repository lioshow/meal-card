package com.lx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value="Record对象", description="")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type= IdType.AUTO)
    private Integer id;

    @TableField("cardNo")
    private Integer cardNo;

    private Integer amount;

    private String action;

    public Record(Integer cardNo, Integer amount, String action)
    {
        this.cardNo = cardNo;
        this.amount = amount;
        this.action = action;
    }

    public Record() {}
}
