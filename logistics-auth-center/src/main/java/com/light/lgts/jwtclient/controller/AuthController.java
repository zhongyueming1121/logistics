package com.light.lgts.jwtclient.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.lgts.framework.jedis.service.JedisClient;
import com.logts.common.result.DyResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class AuthController {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${token.expire.time}")
    private long tokenExpireTime;

    @Value("${refresh.token.expire.time}")
    private Integer refreshTokenExpireTime;

    @Value("${jwt.refresh.token.key.format}")
    private String jwtRefreshTokenKeyFormat;

    @Value("${jwt.blacklist.key.format}")
    private String jwtBlacklistKeyFormat;

    @Autowired
    private JedisClient jedisClient;

    /**
     * 登录授权，生成JWT
     * @param userName
     * @param password
     * @return
     */
    @PostMapping("/auth")
    public DyResult<Map<String,String>> login(@RequestBody String body){
        Map<String,Object> resultMap = new HashMap<>();
        DyResult<Map<String,String>> dyResult = new DyResult<>();
        Jedis jedis = jedisClient.getJedis();
        Gson gson = new Gson();
        Map<String,String> params = gson.fromJson(body,Map.class);
        if(!params.containsKey("userName")||!params.containsKey("password")) {
            dyResult.setStatus(400);
            dyResult.setMsg("userName or password can't be null");
            return dyResult;
        }
        String userName = params.get("userName");
        String password = params.get("password");

        //账号密码校验
        if(StringUtils.equals(userName, "admin") && StringUtils.equals(password, "admin")){
            //生成JWT
            String token = buildJWT(userName);
            //生成refreshToken
            String refreshToken = UUID.randomUUID().toString().replaceAll("-","");
            //保存refreshToken至redis，使用hash结构保存使用中的token以及用户标识
            String refreshTokenKey = String.format(jwtRefreshTokenKeyFormat, refreshToken);
            jedis.hsetnx(refreshTokenKey,"token",token);
            jedis.hsetnx(refreshTokenKey,"userName",userName);
            jedis.expire(refreshToken,refreshTokenExpireTime);
            //返回结果
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("token", token);
            dataMap.put("refreshToken", refreshToken);
            dyResult.setStatus(200);
            dyResult.setMsg("success");
            dyResult.setData(dataMap);
            return dyResult;
        }
        dyResult.setStatus(100);
        dyResult.setMsg(DyResult.ERROR);
        return dyResult;
    }

    /**
     * 刷新JWT
     * @param refreshToken
     * @return
     */
    @GetMapping("/token/refresh")
    public DyResult<String> refreshToken(@RequestParam String refreshToken){
        Map<String,Object> resultMap = new HashMap<>();
        DyResult<String> dyResult = new DyResult<>();
        Jedis jedis = jedisClient.getJedis();
        String refreshTokenKey = String.format(jwtRefreshTokenKeyFormat, refreshToken);
        String userName = jedis.hget(refreshTokenKey,"userName");
        if(StringUtils.isBlank(userName)||"nil".equals(userName)){
            dyResult.setStatus(403);
            dyResult.setMsg("refreshToken过期");
            return dyResult;
        }
        String newToken = buildJWT(userName);
        //替换当前token，并将旧token添加到黑名单
        String oldToken = jedis.hget(refreshTokenKey, "token");
        jedis.hset(refreshTokenKey, "token", newToken);
        jedis.psetex(String.format(jwtBlacklistKeyFormat, oldToken), tokenExpireTime,"oldToken");

        dyResult.setStatus(DyResult.SUCCESS_CODE);
        dyResult.setMsg(DyResult.SUCCESS);
        dyResult.setData(newToken);
        return dyResult;
    }

    private String buildJWT(String userName){
        //生成jwt
        Date now = new Date();
        Algorithm algo = Algorithm.HMAC256(secretKey);
        String token = JWT.create()
                .withIssuer("MING")
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + tokenExpireTime))
                .withClaim("userName", userName)//保存身份标识
                .sign(algo);
        return token;
    }

}