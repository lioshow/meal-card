package com.lx.service;

import com.lx.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.dto.Form;
import com.lx.utils.R;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘肖
 * @since 2023-09-11
 */
public interface IAdminService extends IService<Admin> {
    public R Login(Form form);

    public R Update(Admin admin);

    public R Reset(Admin admin);
}
