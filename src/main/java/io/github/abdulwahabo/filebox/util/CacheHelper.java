package io.github.abdulwahabo.filebox.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Helper that wraps around {@link CacheManager} provides and clean interface for common caching operations.
 */
@Service
public class CacheHelper {

    private CacheManager cacheManager;

    @Autowired
    public CacheHelper(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Puts an item into the given cache using the specified key.
     *
     * @param cacheName The name of the cache to work with.
     * @param key The key for the entry.
     * @param value The item to cache.
     */
    public void put(String cacheName, String key, Object value) {
        Cache cache = cache(cacheName);
        cache.put(key, value);
    }

    /**
     * Returns an item from the specified cache, if present, using the given key.
     *
     * @param cacheName The cache to check.
     * @param key The key to check for.
     * @return An {@link Optional} containing the item if present. Otherwise an empty Optional.
     */
    public Optional<Object> get(String cacheName, String key) {
        Cache cache = cache(cacheName);
        Cache.ValueWrapper valueWrapper = cache.get(key);
        return valueWrapper != null ? Optional.ofNullable(valueWrapper.get()) : Optional.empty() ;
    }

    /**
     * Removes the item, if present, matching the given key from the specified cache.
     *
     * @param cacheName The cache to remove the item from.
     * @param key The key to check for.
     */
    public void remove(String cacheName, String key) {
        Cache cache = cache(cacheName);
        cache.evictIfPresent(key);
    }

    private Cache cache(String name) {
        return cacheManager.getCache(name);
    }
}
