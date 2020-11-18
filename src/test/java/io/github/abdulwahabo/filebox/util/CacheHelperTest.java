package io.github.abdulwahabo.filebox.util;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public class CacheHelperTest {

    private CacheManager cacheManager = mock(CacheManager.class);
    private Cache cookieCache = mock(Cache.class);
    private Cache oauthCache = mock(Cache.class);

    @BeforeAll
    public void setup() {
        // / Todo: use Constants to mock call to cacheManager
        when(cacheManager.getCache(Constants.OAUTH_STATE_CACHE)).thenReturn(oauthCache);
        when(cacheManager.getCache(Constants.COOKIE_CACHE)).thenReturn(cookieCache);
        when(cookieCache.get(anyString())).thenReturn(null); // todo;
        when(oauthCache.get(anyString())).thenReturn(null); // todo;
    }

    @Test
    public void shouldReturnCachedValue() {

    }

    @Test
    public void shouldPutValueInCache() {

    }
}
