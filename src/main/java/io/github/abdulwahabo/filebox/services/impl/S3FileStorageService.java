package io.github.abdulwahabo.filebox.services.impl;

import io.github.abdulwahabo.filebox.exceptions.FileUploadException;
import io.github.abdulwahabo.filebox.services.FileStorageService;
import java.io.File;
import org.springframework.stereotype.Service;

@Service
public class S3FileStorageService implements FileStorageService {

    // todo: deps on AWS SDK.

    @Override
    public String upload(byte[] file) throws FileUploadException {

        return null;
    }

    @Override
    public byte[] download(String id) {
        return new byte[0];
    }

    @Override
    public boolean delete(String id) {
        return false;
    }
}
