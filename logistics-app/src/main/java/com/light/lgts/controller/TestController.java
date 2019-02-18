package com.light.lgts.controller;

import com.diyou.entity.entity.SysUserEntity;
import com.diyou.entity.entity.SysUserEntityCondition;
import com.lgts.framework.jedis.lock.LuaLock;
import com.lgts.framework.jedis.lock.RedisLock;
import com.lgts.framework.jedis.service.JedisClient;
import com.light.framework.starter.mybatis.dao.MybatisDaoImpl;
import com.light.framework.starter.mybatis.mapper.PlusEntityWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
public class TestController {
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private JedisClient jedisClient;

    @PostMapping("/test/redis")
    public Object test1(HttpServletRequest request) {
        log.info("doing...");
        jedisClient.set("AAA","AAA",20);
        LuaLock lock = redisLock.newDefaultLock();
        Boolean s = lock.lock("qqq");
        PlusEntityWrapper<SysUserEntity> mbMemberEntityPlusEntityWrapper = SysUserEntityCondition.builder()
                .andIdEq("10003")
                .build();
        List<SysUserEntity> mbMemberEntities = MybatisDaoImpl.run().selectList(mbMemberEntityPlusEntityWrapper);
        return mbMemberEntities;
    }
}
