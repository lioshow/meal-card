package com.lx.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lx.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 刘肖
 * @since 2023-09-11
 */
public interface StudentMapper extends BaseMapper<Student> {

    @Select("SELECT s.stuNo, s.password, s.stuName, s.gender, s.classInfo, c.cardNo FROM student s LEFT JOIN card c ON s.stuNo = c.stuNo " +
            "WHERE c.cardNo like concat('%', #{value}, '%')")
    Page<Student> selectStudentWithCardNoPage(Page<Student> page, @Param("value") String value);
}
