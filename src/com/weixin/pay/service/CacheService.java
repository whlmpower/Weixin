package com.weixin.pay.service;

import com.weixin.pay.domain.generateOrder.OrderData;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by luc on 16/10/16.
 */
@Service
public class CacheService {
    /**
     * 缓存orderData
     * @param out_trade_no
     * @param orderData
     * @return
     */
    @Cacheable(value = "orderData", key = "#out_trade_no")
    public OrderData handleOrderDateByCache(String out_trade_no, OrderData orderData){
        System.out.println("[add data to cache]");
        return orderData;
    }

    @Cacheable(value = "object", key = "#id")
    public String saveObject(int id){
        System.out.print(id + " save to cache ");
        return id + "save";
    }
}
