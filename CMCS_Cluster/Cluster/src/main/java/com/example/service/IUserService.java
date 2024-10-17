package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.LoginFormDTO;
import com.example.dto.Result;
import com.example.entity.User;

import javax.servlet.http.HttpSession;


public interface IUserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm, HttpSession session);
}
