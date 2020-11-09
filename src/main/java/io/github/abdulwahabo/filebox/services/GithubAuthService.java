package io.github.abdulwahabo.filebox.services;

public interface GithubAuthService {

    /**
     *
     * @param code
     * @return
     */
    String accesstoken(String code);
}
