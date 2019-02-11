package com.lgts.config;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 配置中心启动类
 * @author ymz
 * @date 2018/9/20 14:05
 */
@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication
public class ConfigServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServiceApplication.class,args);
        LoggerFactory.getLogger(ConfigServiceApplication.class).info("==================配置中心服务启动成功==================");
    }
}
