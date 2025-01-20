package com.example.utils.Interceptor;

import com.example.utils.UserHolder;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(HttpMethod.OPTIONS.toString().equals(request.getMethod())){
            System.out.println("OPTIONS请求，放行----------------------------");
            return true;
        }
        //1.判断是否需要拦截（ThreadLocal中是否有用户）
        if (UserHolder.getUser() == null) {
            //没有，需要拦截
            log.debug("拦截请求，未登录");
            System.out.println("拦截请求，未登录");
            response.setStatus(401);
            return false;
        }
        //2.有用户放行
        System.out.println("放行请求，已登录");
        log.debug("放行请求，已登录");
        return true;
    }
}
