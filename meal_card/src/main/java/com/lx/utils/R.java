package com.lx.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

//通用返回结果类，将数据封装成类返回给前端

@Data
public class R<T>
{
    // 编码，1为成功，0为失败
    private Integer code;

    // 错误信息
    private String msg;

    // 数据
    private T data;

    //动态数据
    private Map map = new HashMap();

    public R() {}

    public R(Integer code, String msg, T data)
    {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功结果集
    public static <T> R<T> success(T object, String msg)
    {
        return new R<T>(1,msg, object);
    }

    // 失败结果集
    public static <T> R<T> error(String msg)
    {
        return new R<T>(0,msg, null);
    }

    public R<T> add(String key, Object value)
    {
        this.map.put(key, value);
        return this;
    }
}
