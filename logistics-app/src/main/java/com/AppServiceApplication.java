package com;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * 配置中心启动类
 * @author ymz
 * @date 2018/9/20 14:05
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.light.lgts.dao")
public class AppServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppServiceApplication.class,args);
        LoggerFactory.getLogger(AppServiceApplication.class).info("==================APP服务启动成功==================");
    }
}
