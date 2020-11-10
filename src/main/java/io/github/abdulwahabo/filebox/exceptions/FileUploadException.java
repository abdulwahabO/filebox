package io.github.abdulwahabo.filebox.exceptions;

public class FileUploadException extends Exception {

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileUploadException(String message) {
        super(message);
    }
}
