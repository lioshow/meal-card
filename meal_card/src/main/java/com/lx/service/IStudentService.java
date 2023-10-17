package com.lx.service;

import com.lx.dto.Form;
import com.lx.dto.SearchForm;
import com.lx.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.utils.P;
import com.lx.utils.R;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘肖
 * @since 2023-09-11
 */
public interface IStudentService extends IService<Student> {
    public Student getStudent(Integer stuNo);

    public R Login(Form form);

    public R list(Integer page, Integer size);

    public R updatePassword(Student student);

    public R Reset(Student student);

    public R Save(Student student);

    public R Search(SearchForm searchForm);
}
