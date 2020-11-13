package io.github.abdulwahabo.filebox.exceptions;

public class AwsClientException extends Exception {

    public AwsClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public AwsClientException(String message) {
        super(message);
    }
}
