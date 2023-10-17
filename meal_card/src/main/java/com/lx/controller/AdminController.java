package com.lx.controller;


import com.lx.dto.Form;
import com.lx.entity.Admin;
import com.lx.service.IAdminService;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @GetMapping("/login")
    public R Login(Form form)
    {
        return adminService.Login(form);
    }

    @PutMapping("/update")
    public R Update(@RequestBody Admin admin)
    {
        return adminService.Update(admin);
    }

    @PutMapping("/reset")
    public R Reset(@RequestBody Admin admin)
    {
        return adminService.Reset(admin);
    }
}
