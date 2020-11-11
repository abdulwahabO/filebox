package io.github.abdulwahabo.filebox.exceptions;

public class GithubUserNotFoundException extends Exception {
    public GithubUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GithubUserNotFoundException(String message) {
        super(message);
    }
}
