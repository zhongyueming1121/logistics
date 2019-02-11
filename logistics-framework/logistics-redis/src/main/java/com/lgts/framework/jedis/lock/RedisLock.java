package com.lgts.framework.jedis.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * lua脚本实现的可重入的分布式Redis锁
 *
 * @author ymz
 * @date 2018/9/15 01:21
 */
@Component
public class RedisLock {
    /**
     * 生存时间
     */
    private static final int DEFAULT_LOCK_MAX_EXIST_TIME = 15;

    @Autowired
    Jedis jedis;

    /**
     * 创建一个锁对象
     *
     * @param lockPre          锁前缀，会转换成大写
     * @param lockMaxExistTime 生存时间（秒）
     * @return
     */
    public LuaLock newLock(String lockPre, int lockMaxExistTime) {
        return new LuaLock(jedis, lockPre, lockMaxExistTime);
    }

    /**
     * 创建一个默认的锁对象
     * <li>只能由当前线程解锁，或等待锁过期</li>
     * <li>锁生存时间为5秒</li>
     *
     * @return
     */
    public LuaLock newDefaultLock() {
        return new LuaLock(jedis, "LUA_DEFAULT_LOCK_", DEFAULT_LOCK_MAX_EXIST_TIME);
    }


}
