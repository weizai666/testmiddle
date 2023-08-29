package com.hyts.assemble.redisdelayer.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.redisson.api.map.event.MapEntryListener;

import java.util.concurrent.TimeUnit;

/**
 * describe:
 *
 * @author hxy
 * @date 2020-10-27
 */
@Slf4j
public class RedissonClientTool {

    private RedissonClient redissonClient;

    public RedissonClientTool(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public <T> void addDelayQueueElement(String key, T t, long delay, TimeUnit timeUnit) {
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(key);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.offer(t, delay, timeUnit);
    }

    public <T> T takeDelayQueueElement(String key) {
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(key);
        T t = null;
        try {
            t = blockingFairQueue.take();
        } catch (InterruptedException e) {
            log.error("takeDelayQueueElement error key: " + key, e);
        }
        return t;
    }


    /**
     * 阻塞队列添加元素
     * @param key
     * @param t
     * @param <T>
     */
    public <T> void addBlockingQueueElement(String key, T t) {
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(key);
        blockingFairQueue.offer(t);
    }

    /**
     *
     * 取出队列的元素且删除
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public <T> T pollBlockQueueElement(String key, T t) {
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(key);
        return blockingFairQueue.poll();
    }

    /**
     *
     * 取出队列的元素但是不删除
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public <T> T peekBlockQueueElement(String key, T t) {
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(key);
        return blockingFairQueue.peek();
    }


    /**
     * 队列添加元素
     * @param key
     * @param t
     * @param <T>
     */
    public <T> void addQueueElement(String key, T t) {
        RQueue<T> queue = redissonClient.getQueue(key);
        queue.offer(t);
    }

    /**
     *
     * 取出队列的元素且删除
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public <T> T pollQueueElement(String key, T t) {
        RQueue<T> queue = redissonClient.getQueue(key);
        return queue.poll();
    }

    /**
     *
     * 取出队列的元素但是不删除
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public <T> T peekQueueElement(String key, T t) {
        RQueue<T> queue = redissonClient.getQueue(key);
        return queue.peek();
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public boolean  setObject(String key, Object value) {
        redissonClient.getBucket(key).set(value);
        return true;
    }

    public boolean  removeObject(String key) {
        redissonClient.getBucket(key).delete();
        return true;
    }


    /**
     * 设置过期策略
     * @param key
     * @param value
     * @return
     */
    public boolean  setObject(String key, Object value, Long num, TimeUnit timeUnit) {
        redissonClient.getBucket(key).set(value, num, timeUnit);
        return true;
    }

    public <T> T  getObject(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }


    public <T> RSet<T> getSet(String key) {
        RSet<T> set =  redissonClient.getSet(key);
        return set;
    }

    public boolean deleteSet(String key) {
        return redissonClient.getSet(key).delete();
    }

    public <T> boolean addSetValue(String key, T value) {
        RSet<T> set =  redissonClient.getSet(key);
        return set.add(value);
    }

    public <T> boolean removeSetValue(String key, T value) {
        RSet<T> set =  redissonClient.getSet(key);
        return set.remove(value);
    }

    /**
     * 创建 RMapCache
     * @param mapCacheKey
     * @param mapEntryListener
     */
    public  void createRMapCache(String mapCacheKey, MapEntryListener mapEntryListener) {
        RMapCache rMapCache =  redissonClient.getMapCache(mapCacheKey);
        rMapCache.addListener(mapEntryListener);
    }


    /**
     * 设置 RMapCache key value
     * @param mapCacheKey
     * @param key
     * @param value
     * @param seconds
     */
    public <K, V> void putRMapCacheKeyValue(String mapCacheKey, K key, V value, long seconds) {
        RMapCache rMapCache =  redissonClient.getMapCache(mapCacheKey);
        rMapCache.put(key, value, seconds, TimeUnit.SECONDS);
    }

    public <K, V> V getRMapCacheValue(String mapCacheKey, K key) {
        RMapCache<K, V> rMapCache =  redissonClient.getMapCache(mapCacheKey);
        return rMapCache.get(key);
    }

    public <K,V> void removeRMapCacheKey(String mapCacheKey, K key) {
        RMapCache<K, V> rMapCache =  redissonClient.getMapCache(mapCacheKey);
        rMapCache.fastRemove(key);
    }

    public  <K, V> void  mapPutValue(String mapRedisKey, K key, V value) {
        RMap<K, V> map = redissonClient.getMap(mapRedisKey);
        map.put(key, value);
    }

    public  <K, V> void  mapRemove(String mapRedisKey, K key) {
        RMap<K, V> map = redissonClient.getMap(mapRedisKey);
        map.fastRemove(key);
    }

    public <K, V> RMap <K, V>   getMap(String mapRedisKey, K key, V value) {
        RMap<K, V> map = redissonClient.getMap(mapRedisKey);
        return map;
    }

    public RedissonClient getRedissonClient () {
        return this.redissonClient;
    }

}
