package com.lgts.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jwt.secret")
public class JwtConfig {
    public String key;
    public String[] skip_urls;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getSkip_urls() {
        return skip_urls;
    }

    public void setSkip_urls(String[] skip_urls) {
        this.skip_urls = skip_urls;
    }
}
