package com.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lx.dto.SearchForm;
import com.lx.entity.Record;
import com.lx.entity.Student;
import com.lx.mapper.RecordMapper;
import com.lx.service.IRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.utils.P;
import com.lx.utils.R;
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
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public void insertRecord(Record record)
    {
        recordMapper.insert(record);
    }

    @Override
    public R list(Integer page, Integer size)
    {
        try
        {
            Page<Record> recordPage = new Page<>(page, size);
            Page<Record> resultPage = recordMapper.selectPage(recordPage, null);
            P p = new P(resultPage.getRecords(), resultPage.getTotal());
            return R.success(p, "成功获取所有饭卡记录");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("饭卡记录获取失败");
        }
    }

    @Override
    public R search(SearchForm searchForm)
    {
        try
        {
            Page<Record> recordPage = new Page<>(searchForm.getPage(), searchForm.getSize());
            Page<Record> resultPage = new Page<>();
            if (searchForm.getValue().equals(""))
            {
                resultPage = recordMapper.selectPage(recordPage, null);
            }
            else
            {
                QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
                queryWrapper.like(searchForm.getKey(), searchForm.getValue());
                resultPage = recordMapper.selectPage(recordPage, queryWrapper);
            }
            P p = new P(resultPage.getRecords(), resultPage.getTotal());
            return R.success(p, "搜索成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("搜索失败");
        }
    }

    @Override
    public Record getRecord(Integer id)
    {
        try
        {
            QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            Record record = recordMapper.selectOne(queryWrapper);
            return record;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public R delete(Integer id)
    {
        try
        {
            Record record = this.getRecord(id);
            QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            recordMapper.delete(queryWrapper);
            return R.success(record, "删除成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("删除失败");
        }
    }

    @Override
    public R listByStudent(Student student)
    {
        try
        {
            QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("cardNo", student.getCardNo());
            return R.success(recordMapper.selectList(queryWrapper), "成功获取所有记录");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("获取记录失败");
        }
    }

    @Override
    public R Search(SearchForm searchForm, Student student)
    {
        try
        {
            if (searchForm.getValue().equals(""))
            {
                R r = this.listByStudent(student);
                r.setMsg("获取成功");
                return r;
            }
            else
            {
                QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("cardNo", student.getCardNo());
                queryWrapper.like(searchForm.getKey(), searchForm.getValue());
                return R.success(recordMapper.selectList(queryWrapper), "获取成功");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("获取失败");
        }
    }
}
