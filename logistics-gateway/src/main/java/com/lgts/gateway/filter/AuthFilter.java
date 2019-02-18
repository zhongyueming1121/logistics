package com.lgts.gateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.lgts.framework.jedis.service.JedisClient;
import com.lgts.gateway.config.JwtConfig;
import com.logts.common.result.DyResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AuthFilter implements GatewayFilter, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    private JedisClient jedisClient;

    @Override
    public int getOrder() {
        return -100;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Gson gson = new Gson();
        String url = exchange.getRequest().getURI().getPath();
        //跳过不需要验证的路径
        List<String> skipUrlsList = Arrays.asList(jwtConfig.getSkip_urls());
        for (int i = 0; i < skipUrlsList.size(); i++) {
            if(url.startsWith(skipUrlsList.get(i))) {
                return chain.filter(exchange);
            }
        }
        //从请求头中取出token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        String username = exchange.getRequest().getHeaders().getFirst("Authorization-UserName");
        //未携带token或token在黑名单内
        if (token == null || token.isEmpty() || isBlackToken(token)) {
            log.info("invalid token or black token.");
            ServerHttpResponse originalResponse = exchange.getResponse();
            originalResponse.setStatusCode(HttpStatus.OK);
            originalResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            DyResult<String> dyResult = new DyResult<>();
            dyResult.setStatus(401);
            dyResult.setMsg("401 Unauthorized.");
            byte[] response = gson.toJson(dyResult).getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = originalResponse.bufferFactory().wrap(response);
            return originalResponse.writeWith(Flux.just(buffer));
        }
        //取出token包含的身份
        DyResult<String> tokenCheck = verifyJWT(username,token);
        if(!tokenCheck.isSuccessFul()){
            ServerHttpResponse originalResponse = exchange.getResponse();
            originalResponse.setStatusCode(HttpStatus.OK);
            originalResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            byte[] response = gson.toJson(tokenCheck).getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = originalResponse.bufferFactory().wrap(response);
            return originalResponse.writeWith(Flux.just(buffer));
        }
        //将现在的request，添加当前身份
        ServerHttpRequest mutableReq = exchange.getRequest().mutate().header("Authorization-UserName", tokenCheck.getData()).build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
        return chain.filter(mutableExchange);
    }

    /**
     * JWT验证
     * @param token
     * @return userName
     */
    private DyResult<String> verifyJWT(String userName, String token){
        DyResult<String> dyResult = new DyResult<>();
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.key);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(userName)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            String authUserName = jwt.getClaim("userName").asString();
            if(!authUserName.equals(userName)) {
                dyResult.setStatus(401);
                dyResult.setMsg("invalid token.");
                return dyResult;
            }
            if(!jedisClient.getJedis().exists("AUTH_"+userName)){
                dyResult.setStatus(300);
                dyResult.setMsg("token timeout");
                return dyResult;
            }
            // 成功
            dyResult.setStatus(200);
            dyResult.setStatus(DyResult.SUCCESS_CODE);
            dyResult.setData(authUserName);
            return dyResult;
        } catch (JWTVerificationException e){
            LOGGER.error("token校验错误", e);
        }
        dyResult.setStatus(500);
        dyResult.setMsg("system error");
        return dyResult;
    }

    /**
     * 判断token是否在黑名单内
     * @param token
     * @return
     */
    private boolean isBlackToken(String token){
        assert token != null;
        //todo 黑名单功能
        return false;
    }
}