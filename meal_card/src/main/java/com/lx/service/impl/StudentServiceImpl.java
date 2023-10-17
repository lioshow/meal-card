package com.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lx.dto.Form;
import com.lx.dto.SearchForm;
import com.lx.entity.Card;
import com.lx.entity.Record;
import com.lx.entity.Student;
import com.lx.mapper.StudentMapper;
import com.lx.service.ICardService;
import com.lx.service.IRecordService;
import com.lx.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.utils.P;
import com.lx.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘肖
 * @since 2023-09-11
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ICardService cardService;

    @Autowired
    private IRecordService recordService;

    @Override
    public Student getStudent(Integer stuNo)
    {
        try
        {
            QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stuNo", stuNo);
            Student student = studentMapper.selectOne(queryWrapper);
            // 在card表中获取卡号
            cardService.setCardNo(student);
            return student;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public R Login(Form form)
    {
        try
        {
            Student student = this.getStudent(Integer.parseInt(form.getUsername()));
            if (student == null)
            {
                return R.error("用户名不存在！");
            }
            else
            {
                if (!student.getPassword().equals(form.getPassword()))
                {
                    return R.error("密码错误！");
                }
                else
                {
                    return R.success(student, "登录成功");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("登录失败, 请检查用户名或密码是否正确");
        }
    }

    @Override
    public R Save(Student student)
    {
        try
        {
            QueryWrapper<Card> queryCard = new QueryWrapper<>();
            queryCard.eq("cardNo", student.getCardNo());
            Card c = cardService.getOne(queryCard);
            QueryWrapper<Student> queryStudent = new QueryWrapper<>();
            queryStudent.eq("stuNo", student.getStuNo());
            Student s = studentMapper.selectOne(queryStudent);
            if (c == null && s == null)
            {
                studentMapper.insert(student);
                // 添加学生时添加饭卡和记录
                Card card = new Card(student.getCardNo(), student.getStuNo(), 0);

                Record record = new Record(card.getCardNo(), 0, "新添加饭卡");
                cardService.insertCard(card);
                recordService.insertRecord(record);
                return R.success(student, "添加成功");
            }
            else
            {
                if (c != null) return R.error("添加失败，卡号已存在");
                else return R.error("添加失败，学号已存在");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return R.error("添加失败");
        }
    }

    @Override
    public R list(Integer page, Integer size)
    {
//        try
//        {
//            List<Student> students = studentMapper.selectList(null);
//            // student类里面的cardNo还未赋值，在card表中查找并赋值
//            for (Student student : students)
//            {
//                cardService.setCardNo(student);
//            }
//            return R.success(students, "成功获取所有学生信息");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return R.error("学生信息获取失败");
//        }
        try
        {
            Page<Student> studentPage = new Page<>(page, size);
            Page<Student> resultPage = studentMapper.selectPage(studentPage, null);
            List<Student> students = resultPage.getRecords();
            for (Student student : students)
            {
                cardService.setCardNo(student);
                cardService.setBalance(student);
                cardService.setState(student);
            }
            P p = new P(students, resultPage.getTotal());
            return R.success(p, "成功获取所有学生信息");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("获取学生信息失败");
        }
    }

    @Override
    public R updatePassword(Student student)
    {
        try
        {
            UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("stuNo", student.getStuNo()).set("password", student.getPassword());
            studentMapper.update(null, updateWrapper);
            return R.success(student, "修改成功");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return R.error("修改失败");
        }
    }

    @Override
    public R Reset(Student student)
    {
        try
        {
            student.setPassword(String.valueOf(student.getStuNo()));
            UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("stuNo", student.getStuNo()).set("password", student.getPassword());
            studentMapper.update(null, updateWrapper);
            return R.success(student, "重置成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("重置失败");
        }
    }

    @Override
    public R Search(SearchForm searchForm)
    {
        try
        {
            Page<Student> studentPage = new Page<>(searchForm.getPage(), searchForm.getSize());
            Page<Student> resultPage = new Page<>();
            if (searchForm.getValue().equals(""))
            {
                resultPage = studentMapper.selectPage(studentPage, null);
            }
            else
            {
                if (searchForm.getKey().equals("cardNo")) // 按卡号查询时从card表中获取学号
                {
                    resultPage = studentMapper.selectStudentWithCardNoPage(studentPage, searchForm.getValue());
                }
                else
                {
                    QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
                    queryWrapper.like(searchForm.getKey(), searchForm.getValue());
                    resultPage = studentMapper.selectPage(studentPage, queryWrapper);
                }
            }
            List<Student> students = resultPage.getRecords();
            for (Student student : students)
            {
                cardService.setCardNo(student);
                cardService.setBalance(student);
            }
            P p = new P(students, resultPage.getTotal());
            return R.success(p, "获取成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("获取失败");
        }
    }
}
