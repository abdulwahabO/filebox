package io.github.abdulwahabo.filebox.services;

import io.github.abdulwahabo.filebox.exceptions.FileDeleteException;
import io.github.abdulwahabo.filebox.exceptions.FileDownloadException;
import io.github.abdulwahabo.filebox.exceptions.FileUploadException;

public interface FileStorageService {

    // Returns an ID for the new file. Which is then mapped to it's location by File object.
    String upload(byte[] file, String userEmail) throws FileUploadException;

    byte[] download(String key) throws FileDownloadException;

    boolean delete(String key) throws FileDeleteException;
}
