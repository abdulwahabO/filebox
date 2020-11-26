package io.github.abdulwahabo.filebox.exceptions;

public class AuthenticationException extends Exception {

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
