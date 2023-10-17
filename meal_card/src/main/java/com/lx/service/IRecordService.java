package com.lx.service;

import com.lx.dto.SearchForm;
import com.lx.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.entity.Student;
import com.lx.utils.R;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘肖
 * @since 2023-09-11
 */
public interface IRecordService extends IService<Record> {

    public R listByStudent(Student student);

    public R list(Integer page, Integer size);

    public R search(SearchForm searchForm);

    public void insertRecord(Record record);

    public R Search(SearchForm searchForm, Student student);

    Record getRecord(Integer card);

    public R delete(Integer id);
}
