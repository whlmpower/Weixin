package com.weixin.pay.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by luc on 16/7/28.
 */
/*
不能从根本解决重复通知
1.因为下面的设计中
两个订单同时进来,都已通知过,状态为false
由于,订单号不同,再次发通知时,通知状态被置为true,从而发出模板消息

已有的方案,由于目前微信后台只通知两次(短时间内),所以还是可行的

访问量高的情况下,应该会存在问题(确实存在问题)

2.想了个解决方案
{"order", "status"}
维护一个map的内存
但是担心出现内存溢出的情况(一般不会出现)

3.使用LinkedHashMap LRU缓存算法 维护一个capcity为500的map
解决上两个方案出现的问题
 */
public class NotifyCache<K, V> extends LinkedHashMap<K, V> {

    private int capcity;

    private static ReentrantLock lock = new ReentrantLock();

    public NotifyCache(int capcity) {
        super(capcity, 0.75f, true);
        this.capcity = capcity;
    }

    @Override
    public final V put(K key, V value) {
        lock.lock();
        try {
            return super.put(key, value);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public final V get(Object key) {
        lock.lock();
        try {
            return super.get(key);
        }finally {
            lock.unlock();
        }
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capcity;
    }
}
