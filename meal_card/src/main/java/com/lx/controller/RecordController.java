package com.lx.controller;


import com.lx.dto.Search;
import com.lx.dto.SearchForm;
import com.lx.entity.Student;
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
@RequestMapping("/record")
public class RecordController
{
    @Autowired
    private IRecordService recordService;

    // 管理员端获取所有记录
    @GetMapping("/{page}/{size}")
    public R list(@PathVariable("page") Integer page, @PathVariable("size") Integer size)
    {
        return recordService.list(page, size);
    }

    // 管理员端搜索
    @GetMapping("/search")
    public R search(SearchForm searchForm)
    {
        return recordService.search(searchForm);
    }

    // 学生端搜索
    @PostMapping
    public R Search(@RequestBody Search search)
    {
        SearchForm searchForm = search.getSearchForm();
        Student student = search.getStudent();
        return recordService.Search(searchForm, student);
    }

    @DeleteMapping("{id}")
    public R Delete(@PathVariable("id") Integer id)
    {
        return recordService.delete(id);
    }

    // 学生端获取饭卡记录
    @GetMapping
    public R listByStudent(Student student)
    {
        return recordService.listByStudent(student);
    }
}
