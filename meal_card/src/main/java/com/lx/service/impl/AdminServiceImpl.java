package com.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lx.entity.Admin;
import com.lx.dto.Form;
import com.lx.mapper.AdminMapper;
import com.lx.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘肖
 * @since 2023-09-11
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper Adminmapper;

    @Override
    public R Login(Form form)
    {
        try
        {
            QueryWrapper<Admin> query = new QueryWrapper<>();
            query.eq("name", form.getUsername());
            Admin admin = Adminmapper.selectOne(query);
            if (admin == null)
            {
                return R.error("用户不存在");
            }
            else
            {
                if (!admin.getPassword().equals(form.getPassword()))
                {
                    return R.error("密码错误");
                }
                else
                {
                    return R.success(admin, "登录成功");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("登录失败，请检查用户名或密码是否正确");
        }
    }

    @Override
    public R Update(Admin admin)
    {
        try
        {
            UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("name", admin.getName()).set("password", admin.getPassword());
            Adminmapper.update(null, updateWrapper);
            return R.success(admin, "修改成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("修改失败");
        }
    }

    @Override
    public R Reset(Admin admin)
    {
        try
        {
            String password = "111";
            admin.setPassword(password);
            UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("name", admin.getName()).set("password", admin.getPassword());
            Adminmapper.update(null, updateWrapper);
            return R.success(admin, "重置成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("重置失败");
        }
    }
}
