package io.github.abdulwahabo.filebox.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;

public class CacheHelperTest {

    private CacheManager cacheManager = mock(CacheManager.class);
    private Cache cache = mock(Cache.class);
    private String mockKey = "some_key";
    private String mockValue = "some_value";
    private Cache.ValueWrapper mockValueWrapper = new SimpleValueWrapper(mockValue);
    private String cacheName = "the_cache";

    @BeforeEach
    public void setup() {
        when(cacheManager.getCache(cacheName)).thenReturn(cache);
        when(cache.get(mockKey)).thenReturn(mockValueWrapper);
    }

    @Test
    public void shouldReturnCachedValue() {
        CacheHelper cacheHelper = new CacheHelper(cacheManager);
        String value = (String) cacheHelper.get(cacheName, mockKey).get();
        assertEquals(mockValue, value);
    }

    @Test
    public void shouldPutValueInCache() {
        CacheHelper cacheHelper = new CacheHelper(cacheManager);
        cacheHelper.put(cacheName, mockKey, mockValue);
        verify(cache).put(mockKey, mockValue);
    }

    @Test
    public void shouldRemoveCachedItem() {
        CacheHelper cacheHelper = new CacheHelper(cacheManager);
        cacheHelper.remove(cacheName, mockKey);
        verify(cache).evictIfPresent(mockKey);
    }
}
