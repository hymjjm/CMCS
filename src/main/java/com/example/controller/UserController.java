package com.example.controller;


import com.example.dto.LoginFormDTO;
import com.example.dto.Result;
import com.example.dto.UserDTO;
import com.example.service.IUserService;
import com.example.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

//    @Resource
//    private IUserInfoService userInfoService;

    /**
     * 发送手机验证码
     */
    @PostMapping("code")
    public Result sendCode(@RequestParam("email") String email, HttpSession session) {
        // TODO 发送邮箱验证码并保存验证码
        return userService.sendCode(email,session);
    }

    /**
     * 登录功能
     * @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session){
        // TODO 实现登录功能
        return userService.login(loginForm,session);
    }

//    /**
//     * 登出功能
//     * @return 无
//     */
//    @PostMapping("/logout")
//    public Result logout(){
//        // TODO 实现登出功能
//        return Result.fail("功能未完成");
//    }
//
    @GetMapping("/me")
    public UserDTO me(){
        // TODO 获取当前登录的用户并返回
        UserDTO user = UserHolder.getUser();
        return user;
//        return Result.fail("功能未完成");
    }

//    @GetMapping("/info/{id}")
//    public Result info(@PathVariable("id") Long userId){
//        // 查询详情
//        UserInfo info = userInfoService.getById(userId);
//        if (info == null) {
//            // 没有详情，应该是第一次查看详情
//            return Result.ok();
//        }
//        info.setCreateTime(null);
//        info.setUpdateTime(null);
//        // 返回
//        return Result.ok(info);
//    }
}
