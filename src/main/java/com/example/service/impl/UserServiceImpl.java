package com.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.dto.LoginFormDTO;
import com.example.dto.Result;
import com.example.dto.UserDTO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.IUserService;
import com.example.utils.EmailService;
import com.example.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.utils.RedisConstants.*;
import static com.example.utils.SystemConstants.USER_NICK_NAME_PREFIX;


@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private EmailService emailService;

    @Override
    public Result sendCode(String email, HttpSession session) {
        //1.校验邮箱
        if (RegexUtils.isEmailInvalid(email)) {
            //2.如果不符合，返回错误信息
            return Result.fail("邮箱格式错误！");
        }
        //3.符合，生成验证码
        String code = RandomUtil.randomNumbers(6);

        //4.保存验证码到Redis
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + email,code,LOGIN_CODE_TTL, TimeUnit.MINUTES);
        //5.发送验证码到用户邮箱
        emailService.sendSimpleMessage(email, "验证码", "您的验证码是：" + code);
        log.debug("验证码发送成功，验证码：{}", code);
        //返回ok
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        //1.校验邮箱
        String email = loginForm.getEmail();
        if (RegexUtils.isEmailInvalid(email)) {
            //2.如果不符合，返回错误信息
            return Result.fail("邮箱格式错误！");
        }
        // TODO 3.校验验证码 从Redis获取验证码并校验
        String cacheCode =stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + email);
        String code = loginForm.getCode();
        if (cacheCode == null|| !cacheCode.equals(code)) {
            //3.不一致，报错
            return  Result.fail("验证码错误！");
        }
        //4.一致根据邮箱，查询用户
        User user = query().eq("email", email).one();
        //5.判断用户是不是存在
        if (user == null) {
            //6.用户不存在，创建新用户并保存
            user = createUserWithEmail(email);
        }
        // TODO 7.保存用户信息到Redis ******存成DTO
        // TODO 7.1 随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);
        // TODO 7.2 将User 对象转为HashMap
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new LinkedHashMap<>(),
                CopyOptions.create().setIgnoreNullValue(true).setFieldValueEditor((fieldName, fieldValue) -> fieldValue != null ? fieldValue.toString() : ""));
        // TODO 7.3 存储
        stringRedisTemplate.opsForHash().putAll( LOGIN_USER_KEY+token,userMap);
        // TODO 7.4 设置token有效期
        stringRedisTemplate.expire(LOGIN_USER_KEY+token,60, TimeUnit.MINUTES);
        // TODO 8 返回token
        return Result.ok(token);
    }

    private User createUserWithEmail(String email) {
        //1.创建用户
        User user = new User();
        user.setEmail(email);
        user.setPhone("123");
        user.setNickName(USER_NICK_NAME_PREFIX+RandomUtil.randomString(9));
        //2.保持用户信息
        save(user);
        return user;

    }
}
