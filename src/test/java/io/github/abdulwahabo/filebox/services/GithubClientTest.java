package io.github.abdulwahabo.filebox.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.http.HttpResponse;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GithubClientTest {

    private static MockWebServer mockWebServer = new MockWebServer();
    private String mocKGithubToken = "gh_token_123";
    private String mockUserData = "user_data_json";

    @BeforeAll
    public static void setup() throws Exception {
        mockWebServer.start();
    }

    @Test
    public void shouldSendAccessTokenRequest() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(mocKGithubToken);
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);
        HttpUrl baseUrl = mockWebServer.url("/url");

        GithubClient client = new GithubClient();
        HttpResponse<String> response = client.getAccessToken(baseUrl.toString());
        assertEquals(200, response.statusCode());
        assertEquals(mocKGithubToken, response.body());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/url", request.getPath());
        assertTrue("Application/json".equalsIgnoreCase(request.getHeader("Accept")));
    }

    @Test
    public void shouldReturnUserData() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(mockUserData);
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);
        HttpUrl baseUrl = mockWebServer.url("/url");

        GithubClient client = new GithubClient();
        HttpResponse<String> response = client.getUser("token_1234", baseUrl.toString());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/url", request.getPath());
        assertTrue("application/vnd.github.v3+json".equalsIgnoreCase(request.getHeader("Accept")));
        assertTrue("token token_1234".equalsIgnoreCase(request.getHeader("Authorization")));
    }

    @AfterAll
    public static void tearDown() throws Exception {
        mockWebServer.shutdown();
    }
}
