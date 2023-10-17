package com.lx.dto;


import lombok.Data;

@Data
public class SearchForm
{
    private String key;
    private String value;
    private Integer page;
    private Integer size;
}
