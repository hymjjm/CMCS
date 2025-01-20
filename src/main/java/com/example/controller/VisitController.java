package com.example.controller;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@CrossOrigin
@RestController
@RequestMapping("/api/visits")
public class VisitController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String VISIT_KEY = "website:visits";
    private static final String VISITED_KEY_PREFIX = "cookie:visited:";
    private final Lock lock = new ReentrantLock();

    // 从配置文件中读取的参数，决定是否启用 Redis
    @Value("${redis.enabled:true}") // 默认值为 true
    private boolean redisEnabled;

    @GetMapping("/count")
    public Long getVisitCount() {
        if (!redisEnabled) {
            // Redis 被禁用，直接返回空值
            return null;
        }

        try {
            // 如果 Redis 可用，从 Redis 获取计数
            String count = stringRedisTemplate.opsForValue().get(VISIT_KEY);
            return count != null ? Long.parseLong(count) : 0L;
        } catch (Exception e) {
            // 如果 Redis 操作失败，返回空值
            return null;
        }
    }

    @PostMapping("/increment")
    public void incrementVisitCount(HttpServletRequest request, HttpServletResponse response) {
        if (!redisEnabled) {
            // Redis 被禁用，直接返回
            return;
        }

        try {
            Cookie[] cookies = request.getCookies();
            String userId = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("visited".equals(cookie.getName())) {
                        userId = cookie.getValue();
                        break;
                    }
                }
            }
            if (userId == null) {
                userId = request.getSession().getId();
                Cookie visitedCookie = new Cookie("visited", userId);
                int expiryTimeInSeconds = 60 * 10; // 10分钟
                visitedCookie.setMaxAge(expiryTimeInSeconds);
                visitedCookie.setPath("/");
                visitedCookie.setHttpOnly(true);
                response.addCookie(visitedCookie);
            }
            String visitedKey = VISITED_KEY_PREFIX + userId;
            if (stringRedisTemplate.hasKey(visitedKey)) {
                return;
            }
            lock.lock();
            try {
                stringRedisTemplate.opsForValue().set(visitedKey, "true", 10L, TimeUnit.MINUTES);
                stringRedisTemplate.opsForValue().increment(VISIT_KEY);
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            // 捕获异常，防止 Redis 操作失败导致应用崩溃
            e.printStackTrace();
        }
    }
}
