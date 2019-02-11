package com.lgts.gateway;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Spring boot app.
 *
 * @author shuaicj 2017/10/18
 */
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {

        SpringApplication.run(GatewayApplication.class, args);
        LoggerFactory.getLogger(GatewayApplication.class).info("==========网关启动成功==========");
    }
}
