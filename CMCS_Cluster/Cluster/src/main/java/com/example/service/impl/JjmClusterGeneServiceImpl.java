package com.example.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.entity.JjmClusterGene;
import com.example.mapper.JjmClusterGeneMapper;
import com.example.service.IJjmClusterGeneService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jjm
 * @since 2024-06-12
 */
@Service
public class JjmClusterGeneServiceImpl extends ServiceImpl<JjmClusterGeneMapper, JjmClusterGene> implements IJjmClusterGeneService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String CACHE_KEY = "mutCluster:geneList:All";
    private static final long CACHE_TTL = 2; // 缓存时间（小时）

    @Override
    public List<JjmClusterGene> Redislist() {
        //1.查询redis缓存里有没有
        String genesString = stringRedisTemplate.opsForValue().get(CACHE_KEY);
        //判断是否命中
        if (StrUtil.isNotBlank(genesString)) {
            //非空-有缓存直接返回  把字符json型转换成list对象形式
            List<JjmClusterGene> genesList = JSONUtil.toList(genesString, JjmClusterGene.class);
            return genesList;
        }
        //2.没有缓存 去数据库查找并存入缓存
        List<JjmClusterGene> genesList = list();
        //2.1 要把list对象转换成string（json）类型存入
        String genesListJson = JSONUtil.toJsonStr(genesList);
        stringRedisTemplate.opsForValue().set(CACHE_KEY,genesListJson,CACHE_TTL, TimeUnit.HOURS);

        return genesList;
    }

    @Override
    @Scheduled(cron = "0 0 */2 * * ?") // 每2小时刷新一次缓存
    public void refreshCache() {
        List<JjmClusterGene> genesList = list();
        String genesListJson = JSONUtil.toJsonStr(genesList);
        stringRedisTemplate.opsForValue().set(CACHE_KEY, genesListJson, CACHE_TTL, TimeUnit.HOURS);
    }

}
