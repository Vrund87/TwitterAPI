package com.v1.TwitterWebAPI.dataCache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheTwitter<T> {
    private Cache<String, T> cache;

    //Constructor to build Cache Store
    public CacheTwitter(int expiryDuration, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiryDuration, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public T get(String key) {
        return cache.getIfPresent(key);
    }

    public void add(String key, T value) {
        if(key != null && value != null) {
            cache.put(key, value);
            System.out.println("Record stored in "
                    + value.getClass().getSimpleName()
                    + " Cache with Key = " + key);
        }
    }
}
