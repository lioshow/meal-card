package com.lx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lx.dto.Form;
import com.lx.dto.SearchForm;
import com.lx.entity.Student;
import com.lx.service.IStudentService;
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
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @GetMapping("/login")
    public R Login(Form form)
    {
        return studentService.Login(form);
    }

    @PostMapping
    public R Save(@RequestBody Student student)
    {
        return studentService.Save(student);
    }

    @GetMapping("/{stuNo}")
    public R getStudent(@PathVariable("stuNo") Integer stuNo)
    {
        try
        {
            Student student = studentService.getStudent(stuNo);
            return R.success(student, "成功获取学生信息");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("学生获取失败");
        }
    }

    @GetMapping("/{page}/{size}")
    public R list(@PathVariable("page") Integer page, @PathVariable("size") Integer size)
    {
        return studentService.list(page, size);
    }

    @PutMapping("/updatePassword")
    public R UpdatePassword(@RequestBody Student student)
    {
        try
        {
            boolean flag = studentService.updateById(student);
            if (flag)
            {
                return R.success(student, "修改成功");
            }
            else
            {
                return R.error("修改失败");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("修改失败");
        }
    }

    @PutMapping("/reset")
    public R Reset(@RequestBody Student student)
    {
        return studentService.Reset(student);
    }

    @PutMapping("/update")
    public R Update(@RequestBody Student student)
    {
        try
        {
            boolean flag = studentService.updateById(student);
            if (flag)
            {
                return R.success(student, "修改成功");
            }
            else
            {
                return R.error("修改失败");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("修改失败");
        }
    }

    @DeleteMapping("{stuNo}")
    public R Delete(@PathVariable Integer stuNo)
    {
        try
        {
            Student student = studentService.getStudent(stuNo);
            QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stuNo", stuNo);
            studentService.remove(queryWrapper);
            return R.success(student, "删除成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("删除失败");
        }
    }

    @GetMapping("/search")
    public R Search(SearchForm searchFrom)
    {
        return studentService.Search(searchFrom);
    }
}
