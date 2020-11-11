package io.github.abdulwahabo.filebox.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheHelper {

    private CacheManager cacheManager;

    @Autowired
    public CacheHelper(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void put(String cacheName, String key, Object value) {
        Cache cache = cache(cacheName);
        cache.put(key, value);
    }

    public Optional<Object> get(String cacheName, String key) {
        Cache cache = cache(cacheName);
        Cache.ValueWrapper valueWrapper = cache.get(key);
        return valueWrapper != null ? Optional.ofNullable(valueWrapper.get()) : Optional.empty() ;
    }

    private Cache cache(String name) {
        return cacheManager.getCache(name);
    }
}
