package com.lx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lx.entity.Card;
import com.lx.entity.Record;
import com.lx.service.ICardService;
import com.lx.service.IRecordService;
import com.lx.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 刘肖
 * @since 2023-09-11
 */
@RestController
@RequestMapping("/card")
public class CardController {

    @Autowired
    private ICardService cardService;

    @Autowired
    private IRecordService recordService;

    @PostMapping
    public R Save(@RequestBody Card card)
    {
        try
        {
            boolean flag = cardService.save(card);
            if (flag)
            {
                return R.success(card, "添加成功");
            }
            else
            {
                return R.error("添加失败");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("添加失败");
        }
    }

    @GetMapping("/{cardNo}")
    public R getCard(@PathVariable("cardNo") Integer cardNo)
    {
        try
        {
            return R.success(cardService.getCard(cardNo), "成功获取饭卡信息");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("获取失败");
        }
    }

    @PutMapping
    public R Update(@RequestBody Card card)
    {
        try
        {
            QueryWrapper<Card> query = new QueryWrapper<>();
            query.eq("cardNo", card.getCardNo());
            Integer balance = cardService.getOne(query).getBalance();
            boolean flag = cardService.updateById(card);
            if (flag)
            {
                Record record = new Record(card.getCardNo(), card.getBalance() - balance,"充值");
                recordService.insertRecord(record);
                return R.success(card, "充值成功");
            }
            else
            {
                return R.error("充值失败");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("充值失败");
        }
    }

    @PutMapping("/loss")
    public R Loss(@RequestBody Card card)
    {
        try
        {
            card.setState("已挂失");
            boolean flag = cardService.updateById(card);
            if (flag)
            {
                return R.success(card, "挂失成功");
            }
            else
            {
                return R.error("挂失失败");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("挂失失败");
        }
    }

    @PutMapping("/unhook")
    public R Unhook(@RequestBody Card card)
    {
        try
        {
            card.setState("正常");
            boolean flag = cardService.updateById(card);
            if (flag)
            {
                return R.success(card, "解挂成功");
            }
            else
            {
                return R.error("解挂失败");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("解挂失败");
        }
    }
}
