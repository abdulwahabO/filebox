package io.github.abdulwahabo.filebox.services;

import io.github.abdulwahabo.filebox.exceptions.FileDeleteException;
import io.github.abdulwahabo.filebox.exceptions.FileDownloadException;
import io.github.abdulwahabo.filebox.exceptions.FileUploadException;

/**
 *
 */
public interface FileStorageService {

    /**
     *
     * @param file
     * @param userEmail
     * @return
     * @throws FileUploadException
     */
    String upload(byte[] file, String userEmail) throws FileUploadException;

    /**
     *
     *
     * @param key
     * @return
     * @throws FileDownloadException
     */
    byte[] download(String key) throws FileDownloadException;

    /**
     *
     * @param key
     * @return
     * @throws FileDeleteException
     */
    boolean delete(String key) throws FileDeleteException;
}
