package com.lgts.gateway.config;

import com.lgts.gateway.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ymz
 * @date 2018/9/12 22:15
 */
@Configuration
public class GatewayConfiguration {
    @Autowired
    private AuthFilter authFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                //api
                .route(r -> r.path("/**")
                        .filters(f -> f.filter(authFilter)
                        )
                        .uri("lb://API-APP")
                )
                .build();
    }

}
