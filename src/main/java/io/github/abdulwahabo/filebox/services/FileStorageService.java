package io.github.abdulwahabo.filebox.services;

import io.github.abdulwahabo.filebox.exceptions.FileUploadException;

public interface FileStorageService {

    // Returns an ID for the new file. Which is then mapped to it's location by File object.
    String upload(byte[] file) throws FileUploadException;

    byte[] download(String id);

    boolean delete(String id);
}
