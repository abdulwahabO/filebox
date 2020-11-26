package io.github.abdulwahabo.filebox.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

/**
 * HTTP client for making requests to Github's API.
 */
@Service
public class GithubClient {

    public HttpResponse<String> getAccessToken(String url) throws IOException, InterruptedException, URISyntaxException {

        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                                         .POST(HttpRequest.BodyPublishers.noBody())
                                         .header("Accept", "application/json")
                                         .build();

        HttpClient client = httpClient();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getUser(String accessToken, String url) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                                         .header("Accept", "application/vnd.github.v3+json")
                                         .header("Authorization", "token " + accessToken)
                                         .GET()
                                         .build();

        HttpClient client = httpClient();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpClient httpClient() {
        return HttpClient.newBuilder()
                         .version(HttpClient.Version.HTTP_1_1)
                         .followRedirects(HttpClient.Redirect.NEVER)
                         .build();
    }
}
