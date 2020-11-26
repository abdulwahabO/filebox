package io.github.abdulwahabo.filebox.exceptions;

public class FileDownloadException extends Exception {

    public FileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDownloadException(String message) {
        super(message);
    }
}
