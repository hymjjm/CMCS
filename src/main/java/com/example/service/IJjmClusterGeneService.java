package com.example.service;

import com.example.entity.JjmClusterGene;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jjm
 * @since 2024-06-12
 */
public interface IJjmClusterGeneService extends IService<JjmClusterGene> {

    List<JjmClusterGene> Redislist();

    @Scheduled(cron = "0 0 */2 * * ?") // 每2小时刷新一次缓存
    void refreshCache();
}
