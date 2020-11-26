package io.github.abdulwahabo.filebox.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.abdulwahabo.filebox.services.dto.GithubAccessTokenDto;
import io.github.abdulwahabo.filebox.services.dto.GithubUserDto;
import io.github.abdulwahabo.filebox.util.CacheHelper;

import java.net.http.HttpResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GithubAuthServiceTest {

    private GithubClient githubClient = mock(GithubClient.class);
    private CacheHelper cacheHelper = mock(CacheHelper.class);

    private HttpResponse<String> accessTokenResponse = GithubServiceTestHelper.accessTokenResponse();
    private HttpResponse<String> userDataResponse = GithubServiceTestHelper.userDataResponse();

    @BeforeEach
    public void setup() throws Exception {
        doNothing().when(cacheHelper).put(anyString(), anyString(), any());
        when(githubClient.getAccessToken(anyString())).thenReturn(accessTokenResponse);
        when(githubClient.getUser(anyString(), anyString())).thenReturn(userDataResponse);
    }

    @Test
    public void shouldGetAccessToken() throws Exception {
        GithubAuthService authService = new GithubAuthService(cacheHelper, githubClient);
        GithubAccessTokenDto tokenDto = authService.accesstoken("some_code");
        assertEquals(GithubServiceTestHelper.MOCK_ACCESS_TOKEN, tokenDto.getToken());
    }

    @Test
    public void shouldReturnUserForAccessToken() throws Exception {
        GithubAuthService authService = new GithubAuthService(cacheHelper, githubClient);
        GithubUserDto userDto = authService.getGithubUser("access_token");
        assertEquals(GithubServiceTestHelper.MOCK_USER_EMAIL, userDto.getEmail());
    }
}
