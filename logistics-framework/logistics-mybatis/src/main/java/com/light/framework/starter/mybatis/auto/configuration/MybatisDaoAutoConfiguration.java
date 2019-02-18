package com.light.framework.starter.mybatis.auto.configuration;

import com.baomidou.mybatisplus.spring.boot.starter.MybatisPlusAutoConfiguration;
import com.light.framework.starter.mybatis.dao.MybatisDao;
import com.light.framework.starter.mybatis.dao.MybatisDaoImpl;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Created by ZHL on 17/4/6.
 */
@Configuration
@AutoConfigureAfter({MybatisPlusAutoConfiguration.class})
public class MybatisDaoAutoConfiguration {

    @SuppressWarnings({"SpringJavaAutowiringInspection", "SpringJavaInjectionPointsAutowiringInspection"})
    @Bean
    @ConditionalOnMissingBean
    public MybatisDao mybatisDao(SqlSessionTemplate sqlSessionTemplate) {
        return new MybatisDaoImpl(sqlSessionTemplate);
    }

    @Bean
    public MybatisApplicationContext mybatisApplicationContext() {
        return new MybatisApplicationContext();
    }

    /*@Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    public MybatisRedisCacheWraper mybatisRedisCacheWraper(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisSerializer<Object> valueSerializer = new GenericJackson2JsonRedisSerializer();
        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setDefaultSerializer(valueSerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        MybatisRedisCacheWraper mybatisRedisCacheWraper = new MybatisRedisCacheWraper();
        mybatisRedisCacheWraper.setRedisTemplate(redisTemplate);
        return mybatisRedisCacheWraper;
    }*/
}
