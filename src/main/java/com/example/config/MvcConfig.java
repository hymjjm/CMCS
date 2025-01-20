package com.example.config;

import com.example.utils.Interceptor.LoginInterceptor;
import com.example.utils.Interceptor.RefreshTokenInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${app.enable-interceptor:true}") // 从配置文件读取是否启用拦截器的参数
    private boolean enableInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (enableInterceptor) {
            // 如果拦截器启用，注册 RefreshTokenInterceptor 和 LoginInterceptor
            registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).order(0);

            registry.addInterceptor(new LoginInterceptor())
                    .excludePathPatterns(
                            "/jjm-cluster-upload/cancerType/**",
                            "/jjm-cluster-upload/Sample/**",
                            "/jjm-cluster-upload/ReferenceGenomes/**",
                            "/jjm-cluster-upload/selectPage/**",
                            "/jjm-cluster-upload/MutCluster/**",
//                        "/jjm-cluster-upload/deduplicateData",
                            "/api/visits/**",
                            "/jjm-cluster-gene/**",
//                        "/R/**",
                            "/user/code",
                            "/user/me",
                            "/user/login"
                    ).order(1);
        }
        // 如果拦截器关闭，则不注册任何拦截器，相当于所有请求直接放行
    }
}
