package io.github.abdulwahabo.filebox.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.abdulwahabo.filebox.exceptions.AuthenticationException;
import io.github.abdulwahabo.filebox.exceptions.UserNotFoundException;
import io.github.abdulwahabo.filebox.services.dto.GithubAccessTokenDto;
import io.github.abdulwahabo.filebox.services.dto.GithubUserDto;
import io.github.abdulwahabo.filebox.util.CacheHelper;
import io.github.abdulwahabo.filebox.util.Constants;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GithubAuthService {

    private final String GITHUB_AUTHORIZE_URL = "https://github.com/login/oauth/authorize?";
    private final String GITHUB_TOKEN_URL = "https://github.com/login/oauth/access_token?";
    private final String GITHUB_USER_API_URL = "https://api.github.com/user";

    @Value("${app.host}")
    private String host;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String secret;

    private CacheHelper cacheHelper;
    private GithubClient githubClient;

    @Autowired
    public GithubAuthService(CacheHelper cacheHelper, GithubClient githubClient) {
        this.cacheHelper = cacheHelper;
        this.githubClient = githubClient;
    }

    /**
     *
     */
    public GithubAccessTokenDto accesstoken(String code) throws AuthenticationException {
        String params = String.format("client_id=%s&client_secret=%s&code=%s", clientId, secret, code);
        try {
            HttpResponse<String> response = githubClient.getAccessToken(GITHUB_TOKEN_URL.concat(params));

            if (!(response.statusCode() >= 200 && response.statusCode() <= 299)) {
                throw new AuthenticationException("Failed to obtain Github access token");
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), GithubAccessTokenDto.class);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new AuthenticationException("Failed to obtain Github access token", e);
        }
    }

    /**
     *
     */
    public String redirectUrl() {
        host = startUrlWithoutSlash(host);
        String redirect = host.concat(Constants.OAUTH_CALLBACK_URL);
        String format = "client_id=%s&redirect_uri=%s&state=%s&allow_signup=false";
        String state = random();
        cacheHelper.put(Constants.OAUTH_STATE_CACHE, state, "valid_state");
        String params = String.format(format, clientId, redirect, state);
        return GITHUB_AUTHORIZE_URL.concat(params);
    }

    /**
     *
     * @param accessToken
     * @return
     * @throws UserNotFoundException
     */
    public GithubUserDto getGithubUser(String accessToken) throws AuthenticationException {
        try {
            HttpResponse<String> response = githubClient.getUser(accessToken, GITHUB_USER_API_URL);
            if (!(response.statusCode() >= 200 && response.statusCode() <= 299)) {
                throw new AuthenticationException("Failed to obtain Github access token");
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), GithubUserDto.class);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new AuthenticationException("Failed to obtain Github access token", e);
        }
    }

    private String random() {
        Random random = new Random();
        return String.valueOf(random.nextInt(54512));
    }

    private String startUrlWithoutSlash(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }
}
