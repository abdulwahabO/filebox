package io.github.abdulwahabo.filebox.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for binding to Github API JSON for OAuth2 access tokens.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubAccessTokenDto {

    @JsonProperty("access_token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
