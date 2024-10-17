package com.example.ose;

import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class CacheTest {

    @Resource
    private CacheClient cacheClient;
//    @Test
//    void testSaveShop(){
//        Shop shop = shopService.getById(1L);
//        cacheClient.setWithLogicalExpire(CACHE_SHOP_KEY+  1L, shop,10L, TimeUnit.SECONDS);
//    }

}
