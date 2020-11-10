package io.github.abdulwahabo.filebox.services;

import java.net.http.HttpClient;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GithubAuthService {

    private final String GITHUB_BASE_URL = "https://github.com/login/oauth/authorize?";
    private final String OAUTH_STATE_CACHE = "state_cache";

    private CacheHelper cacheHelper;

    @Autowired
    public GithubAuthService(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }

    @Value("${app.host}")
    private String host;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String secret;

    /**
     *
     * @param code
     * @return
     */
    public String accesstoken(String code){

        // todo: use Java 11 HttpClient...

        return null;
    }

    public String redirectUrl() {
        String redirect = host.concat("/auth/callback");
        String format = "client_id=%sredirect_uri=%sstate=%sallow_signup=false";
        String state = random();
        cacheHelper.put(OAUTH_STATE_CACHE, state, "valid_state");
        String params = String.format(format, clientId, redirect, state);
        return GITHUB_BASE_URL.concat(params);
    }

    private String random() {
        Random random = new Random();
        return String.valueOf(random.nextInt(54512));
    }
}
