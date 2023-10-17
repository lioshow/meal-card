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
@ApiModel(value="Card对象", description="")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "cardNo")
    private Integer cardNo;

    @TableField("stuNo")
    private Integer stuNo;

    private Integer balance;

    private String state;

    public Card(Integer cardNo, Integer stuNo, Integer balance)
    {
        this.cardNo = cardNo;
        this.stuNo = stuNo;
        this.balance = balance;
        this.state = "正常";
    }

    public Card() {}
}
