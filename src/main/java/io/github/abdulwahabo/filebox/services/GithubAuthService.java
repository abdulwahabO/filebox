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

    // TODO: These are package-private so that a test can change their values before they are passed down to the Client
    final String GITHUB_AUTHORIZE_URL = "https://github.com/login/oauth/authorize?";
    final String GITHUB_TOKEN_URL = "https://github.com/login/oauth/access_token?";
    final String GITHUB_USER_API_URL = "https://api.github.com/user";

    private CacheHelper cacheHelper;
    private GithubClient githubClient;

    @Autowired
    public GithubAuthService(CacheHelper cacheHelper, GithubClient githubClient) {
        this.cacheHelper = cacheHelper;
        this.githubClient = githubClient;
    }

    @Value("${app.host}")
    private String host;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String secret;

    /**
     *
     */
    public GithubAccessTokenDto accesstoken(String code) throws AuthenticationException {
        String params = String.format("client_id=%sclient_secret=%scode=%s", clientId, secret, code);
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

    // TODO: Implement CLoudWatch for App in Production.

    /**
     *
     */
    public String redirectUrl() {
        String redirect = host.concat(Constants.OAUTH_CALLBACK_URL);
        String format = "client_id=%sredirect_uri=%sstate=%sallow_signup=false";
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
    public GithubUserDto getGithubUser(String accessToken) throws UserNotFoundException {
        try {
            HttpResponse<String> response = githubClient.getUser(accessToken, GITHUB_USER_API_URL);
            if (!(response.statusCode() >= 200 && response.statusCode() <= 299)) {
                throw new UserNotFoundException("Failed to obtain Github access token");
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), GithubUserDto.class);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new UserNotFoundException("Failed to obtain Github access token", e);
        }
    }

    private String random() {
        Random random = new Random();
        return String.valueOf(random.nextInt(54512));
    }
}
