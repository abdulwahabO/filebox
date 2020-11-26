package io.github.abdulwahabo.filebox.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Model for binding to Github API user JSON.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubUserDto {

    private String email;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
