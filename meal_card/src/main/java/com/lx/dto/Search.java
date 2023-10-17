package com.lx.dto;

import com.lx.entity.Student;
import lombok.Data;

@Data
public class Search
{
    private SearchForm searchForm;
    private Student student;
}
