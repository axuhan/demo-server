package com.xh.agentdemo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean("CaffeineCacheManager")
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.registerCustomCache("CredentialKeyPairCache",
                Caffeine.newBuilder()
                        .initialCapacity(1000)
                        .maximumSize(1000)
                        .expireAfterWrite(2, TimeUnit.MINUTES)
                        .build());
        return cacheManager;
    }
}
