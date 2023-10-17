package com.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lx.entity.Card;
import com.lx.entity.Student;
import com.lx.mapper.CardMapper;
import com.lx.service.ICardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘肖
 * @since 2023-09-11
 */
@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements ICardService {

    @Autowired
    private CardMapper cardMapper;

    @Override
    public void setCardNo(Student student)
    {
        try
        {
            QueryWrapper<Card> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stuNo", student.getStuNo());
            student.setCardNo(cardMapper.selectOne(queryWrapper).getCardNo());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void setBalance(Student student)
    {
        try
        {
            QueryWrapper<Card> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stuNo", student.getStuNo());
            student.setBalance(cardMapper.selectOne(queryWrapper).getBalance());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void setState(Student student)
    {
        try
        {
            QueryWrapper<Card> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stuNo", student.getStuNo());
            student.setState(cardMapper.selectOne(queryWrapper).getState());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void insertCard(Card card)
    {
        cardMapper.insert(card);
    }

    @Override
    public Card getCard(Integer cardNo)
    {
        try
        {
            QueryWrapper<Card> cardQuery = new QueryWrapper<>();
            cardQuery.eq("cardNo", cardNo);
            Card card = cardMapper.selectOne(cardQuery);
            return card;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
