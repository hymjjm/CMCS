package com.example.utils.CachesRedis;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@Component
public class CacheClient {
    private StringRedisTemplate stringRedisTemplate;

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void set(String key, Object value, Long time, TimeUnit unit){
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(value),time,unit);
    }
//    逻辑过期
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit){
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(redisData));
    }

    //缓存穿透
    public <R,ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID,R> dbFallback,
                                         Long time, TimeUnit unit ) {
        String key = keyPrefix + id;
        //1.从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if (StrUtil.isNotBlank(json)) { //因为缓存空对象是空值，命中的是空值还是会走下面4，所以要加个空值判断，
            //3.存在，存在直接返回
            return JSONUtil.toBean(json,type);
        }
        //TODO 判断命中的是否是空值""(空值和null不一样),    一定要是！= null
        if (json != null) {
            return null;
        }
        //4.不存在，根据id查询数据库
        R r = dbFallback.apply(id);
        //5.不存在，写入空值
        if (r == null) {
            stringRedisTemplate.opsForValue().set(key,"", 5L, TimeUnit.MINUTES);
            return null;
        }
        //6.存在，写入redis
        this.set(key,r,time,unit);
        //返回
        return r;
    }

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    //缓存击穿(热点id)--逻辑过期
    //因为不会过期（所有数据都已经提前放进去了），所以没命中的时候直接返回空结束，也就没有缓存穿透问题
    public <R,ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type, Function<ID,R> dbFallback,
                                       Long time, TimeUnit unit ,String lockKeyPrefix) {
        String key = keyPrefix + id;
        //1.从redis查询商铺缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if (StrUtil.isBlank(json)) {
            //3.未命中直接返回
            return null;
        }
        //4. 命中，先把json格式数据反序列化为对象
        RedisData RedisData = JSONUtil.toBean(json, RedisData.class);
        R r = JSONUtil.toBean((JSONObject) RedisData.getData(), type);
        //5 判断是不是过期
        LocalDateTime expireTime = RedisData.getExpireTime();
        if (expireTime.isAfter(LocalDateTime.now())) {
            //5.1 未过期，直接返回店铺信息
            return r;
        }
        //5.2 已过期，需要缓存重建
        //6 缓存重建
        //6.1 获取互斥锁
        String lockKey = lockKeyPrefix + id;
        Boolean isLock = tryLock(lockKey);
        //6.2 判断是否获取锁成功
        if(isLock){
            //二次check，缓存未过期，直接返回   如果这个时候别的线程已经重建好缓存了就不用再重建了，所以要做二次check， 就是这个时候也可能是别的已经重建后释放的锁
            //1.从redis查询商铺缓存
            json = stringRedisTemplate.opsForValue().get(key);
            //2.判断是否存在
            if (StrUtil.isBlank(json)) {
                //3.未命中直接返回
                return null;
            }
            //4. 命中，先把json格式数据反序列化为对象
            RedisData = JSONUtil.toBean(json, RedisData.class);
            r = JSONUtil.toBean((JSONObject) RedisData.getData(), type);
            //5 判断是不是过期
            expireTime = RedisData.getExpireTime();
            if (expireTime.isAfter(LocalDateTime.now())) {
                //5.1 未过期，直接返回店铺信息
                return r;
            }
            //TODO 6.3 成功，开启独立线程，实现缓存重建    线程池
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                //缓存重建
                try {
                    R r1 = dbFallback.apply(id);
                    setWithLogicalExpire(key, r1, time,unit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    unlock(lockKey);
                }

            });

        }

        //6.4 返回过期的商铺信息
        return r;
    }

    private boolean tryLock(String key){
        //这里的flag是包装类，如果直接返回flag，自动拆箱可能会出问题，所以用工具类来
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private  void unlock(String key){
        stringRedisTemplate.delete(key);
    }

}
