package com.light.lgts;

import com.lgts.framework.jedis.EnableYmRedis;
import com.light.lgts.jwtclient.filter.CurrentUserResolver;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringCloudApplication
@EnableYmRedis
public class JwtClientApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(JwtClientApplication.class, args);
		LoggerFactory.getLogger(JwtClientApplication.class).info("==================AUTH服务启动成功==================");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new CurrentUserResolver());
	}

}

