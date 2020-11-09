package io.github.abdulwahabo.filebox.services;

public interface FileStorageService {

    // Returns an ID for the new file. Which is then mapped to it's location by File object.
    String upload(byte[] file);

    byte[] download(String id);

    boolean delete(String id);
}
