package com.v1.TwitterWebAPI.dataCache;

import com.v1.TwitterWebAPI.models.TwitterUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheStoreBeans {
    @Bean
    public CacheTwitter<TwitterUser> twitterUserCache() {
        return new CacheTwitter<TwitterUser>(5, TimeUnit.MINUTES);
    }
}
