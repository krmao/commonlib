package com.simple.core.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.Set;

/**
 * redis操作帮助类
 * @author hejinguo
 * @version $Id: JedisHelper.java, v 0.1 2019年11月17日 下午5:35:06
 */
public class JedisHelper {
    /**redis线程池*/
    private static JedisPool jedisPool = null;

    private JedisHelper() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(JedisConfig.redis_maxActive);
        config.setMaxIdle(JedisConfig.redis_maxIdle);
        config.setMaxWaitMillis(JedisConfig.redis_maxWait);
        config.setTestOnBorrow(JedisConfig.redis_testOnBorrow);
        config.setBlockWhenExhausted(true);
        jedisPool = new JedisPool(config, JedisConfig.redis_hostName, JedisConfig.redis_hostPort,3000, JedisConfig.redis_passWord);
    }

    private static class JedisHelperHolder {
        private static JedisHelper INSTACE = new JedisHelper();
    }

    public static JedisHelper getInstance() {
        return JedisHelper.JedisHelperHolder.INSTACE;
    }

    /**
     * String字符串设置值
     * @param key  键
     * @param t  值
     * @param selectDb  数据库ID
     */
    public <T> void set(String key, T t, JedisDBEnum selectDb) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        jedis.set(key, String.valueOf(t));
        this.returnResource(jedis);
    }

    /**
     * 判断Key是否存在
     * @param key
     * @param selectDb
     * @return
     */
    public boolean exists(String key, JedisDBEnum selectDb) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        boolean isExists = jedis.exists(key);
        this.returnResource(jedis);
        return isExists;
    }

    /**
     * map设置值
     * @param key
     * @param map
     */
    public <T> void setMap(String key, Map<String, String> map, JedisDBEnum selectDb) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        jedis.hmset(key, map);
        this.returnResource(jedis);
    }

    /**
     * 获取Map所有key和value
     * @param key
     * @param selectDb
     * @return
     */
    public Map<String, String> getMap(String key, JedisDBEnum selectDb) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        Map<String, String> resultMap = jedis.hgetAll(key);
        this.returnResource(jedis);
        return resultMap;
    }

    /**
     * 获取Map中是否存在Key
     * @param key
     * @param filed
     * @param selectDb
     * @return
     */
    public boolean hexists(String key, String field, JedisDBEnum selectDb) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        boolean isExists = jedis.hexists(key, field);
        this.returnResource(jedis);
        return isExists;
    }

    /**
     * List尾部设置值
     * @param key
     * @param map
     */
    public <T> void setRumpPushList(String key, JedisDBEnum selectDb, final String... strings) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        jedis.rpush(key, strings);
        this.returnResource(jedis);
    }

    /**
     * 当key不存时保存value
     * @param key  键
     * @param t  值
     * @param selectDb  数据库ID
     */
    public <T> Boolean setnx(String key, T t, JedisDBEnum selectDb) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        long result = jedis.setnx(key, String.valueOf(t));
        this.returnResource(jedis);
        return result == 1 ? true : false;
    }

    /**
     * 将值value关联到key，并将key的生存时间设为seconds(以秒为单位)
     * @param key
     * @param seconds
     * @param t
     */
    public <T> void setex(String key, Integer seconds, T t, JedisDBEnum selectDb) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        jedis.setex(key, seconds, String.valueOf(t));
        this.returnResource(jedis);
    }

    /**
     * 返回key所关联的字符串值
     * 
     * @param key
     * @return
     */
    public String get(String key, JedisDBEnum selectDb) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        String value = jedis.get(key);
        this.returnResource(jedis);
        return value;
    }

    /**
     * 获取全部set集合值(从小到大)
     * @param key
     * @param selectDb
     * @return
     */
    public Set<String> getSet(String key, JedisDBEnum selectDb) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        Set<String> value = jedis.zrange(key, 0, -1);
        this.returnResource(jedis);
        return value;
    }

    /**
     * key添加指定增量后返回结果值(key为空则创建默认值0后+increment增量后返回)
     * @param key
     * @param increment
     * @param selectDb
     * @return
     */
    public Long getIncrBy(String key, long increment, JedisDBEnum selectDb) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        Long value = jedis.incrBy(key, increment);
        this.returnResource(jedis);
        return value;
    }

    /**
     * 设置key过期时间(秒)
     * @param key
     * @param second
     * @param selectDb
     */
    public void setExpireSeconds(String key, int seconds, JedisDBEnum selectDb) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        jedis.expire(key, seconds);
        this.returnResource(jedis);
    }

    /**
     * 设置key过期时间(毫秒秒)
     * @param key
     * @param second
     * @param selectDb
     */
    public void setExpireMillisTime(String key, long millisTime, JedisDBEnum selectDb) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        jedis.pexpire(key, millisTime);
        this.returnResource(jedis);
    }

    /**
     * 释放reids连接资源
     * @param jedis
     */
    public void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 删除 redis 存储
     * @param key
     * @param selectDb
     */
    public void del(String key, JedisDBEnum selectDb) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(selectDb.getValue());
        jedis.del(key);
        this.returnResource(jedis);
    }
}
