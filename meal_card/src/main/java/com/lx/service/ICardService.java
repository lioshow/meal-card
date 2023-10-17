package com.lx.service;

import com.lx.dto.SearchForm;
import com.lx.entity.Card;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.entity.Student;
import com.lx.utils.R;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘肖
 * @since 2023-09-11
 */
public interface ICardService extends IService<Card> {
    public void setCardNo(Student student);

    public void setBalance(Student student);

    public void setState(Student student);

    public void insertCard(Card card);

    public Card getCard(Integer cardNo);
}
