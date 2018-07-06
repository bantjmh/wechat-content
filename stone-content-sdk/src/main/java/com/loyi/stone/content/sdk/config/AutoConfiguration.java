package com.loyi.stone.content.sdk.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.loyi.stone.content.client")
public class AutoConfiguration {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        LOG.info("stone-content-sdk init");
    }
}
