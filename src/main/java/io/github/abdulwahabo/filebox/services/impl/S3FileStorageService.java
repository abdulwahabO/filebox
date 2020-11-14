package io.github.abdulwahabo.filebox.services.impl;

import io.github.abdulwahabo.filebox.exceptions.FileDeleteException;
import io.github.abdulwahabo.filebox.exceptions.FileDownloadException;
import io.github.abdulwahabo.filebox.exceptions.FileUploadException;
import io.github.abdulwahabo.filebox.services.AwsS3Client;
import io.github.abdulwahabo.filebox.services.FileStorageService;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class S3FileStorageService implements FileStorageService {

    private AwsS3Client s3Client;

    @Autowired
    public S3FileStorageService(AwsS3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String upload(byte[] file, String userEmail) throws FileUploadException {
        String key = generateKey(userEmail);
        boolean success = s3Client.uploadFile(key, file);
        if (!success) {
            throw new FileUploadException("File Upload failed");
        }
        return key;
    }

    @Override
    public byte[] download(String key) throws FileDownloadException {
        Optional<byte[]> bytesOpt = s3Client.downloadFile(key);
        return bytesOpt.orElseThrow(() -> new FileDownloadException("Failed to download file"));
    }

    @Override
    public boolean delete(String key) throws FileDeleteException {
        return s3Client.deleteFile(key);
    }

    private String generateKey(String email) {
        Random random = new Random();
        return String.format("%s:%s:%s", email, System.currentTimeMillis(), random.nextInt(15000));
    }
}
