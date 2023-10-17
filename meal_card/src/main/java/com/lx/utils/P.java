package com.lx.utils;

import lombok.Data;

@Data
public class P
{
    private Object data;
    private Long total;

    public P(Object data, Long total)
    {
        this.data = data;
        this.total = total;
    }

    public P() {}
}
