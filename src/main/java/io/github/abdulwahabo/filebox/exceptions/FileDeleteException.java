package io.github.abdulwahabo.filebox.exceptions;

public class FileDeleteException extends Exception {
    public FileDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDeleteException(String message) {
        super(message);
    }
}
