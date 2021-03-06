package com.lgts.framework.jedis;

import com.lgts.framework.jedis.config.JedisConfig;
import com.lgts.framework.jedis.lock.RedisLock;
import com.lgts.framework.jedis.service.JedisClient;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 集群版jedis
 * @author ymz
 * @date 2018/10/26 20:13
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({JedisConfig.class, RedisLock.class, JedisClient.class})
public @interface EnableYmRedis {

}
