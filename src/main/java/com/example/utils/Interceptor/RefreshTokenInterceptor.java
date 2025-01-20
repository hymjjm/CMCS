package com.example.utils.Interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.example.dto.UserDTO;
import com.example.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.utils.RedisConstants.LOGIN_USER_KEY;


public class RefreshTokenInterceptor implements HandlerInterceptor {
    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("接收到请求方法_________________: " + request.getMethod());
        if(HttpMethod.OPTIONS.toString().equals(request.getMethod())){
            System.out.println("OPTIONS请求，放行----------------------------");
           return true;
       }
//        //TODO  1.获取请求头中的token
        String token = request.getHeader("Authorization");

        System.out.println("_________________token:"+ token);
        if (StrUtil.isBlank(token)) {
            //不存在不拦截，直接放行
//            response.setStatus(401);
            return true;
        }
        //TODO 2.基于token获取Redis中的用户(token就是Redis的Key)
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(LOGIN_USER_KEY + token);
        System.out.println("----------Redis中的用户(token就是Redis的Key):"+userMap);
        //3.判断用户是否存在
        if (userMap.isEmpty()) {
            //4.不存在不拦截，直接放行
//            response.setStatus(401);
            return true;
        }
        //TODO 5.将查询到的Hash数据转换为UserDTO对象
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);

        //6.存在，保存用户信息到ThreadLocal
        UserHolder.saveUser(userDTO);
//        System.out.println("------------------------UserHolder.getUser():"+UserHolder.getUser());
        //TODO 7. 刷新token有效期
        stringRedisTemplate.expire(LOGIN_USER_KEY + token,60, TimeUnit.MINUTES);
        //8.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除用户，避免内存泄露
        System.out.println("_____________移除用户，避免内存泄露:"+ UserHolder.getUser());

        UserHolder.removeUser();
    }
}
