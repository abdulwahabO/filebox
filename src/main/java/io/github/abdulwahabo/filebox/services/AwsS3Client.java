package io.github.abdulwahabo.filebox.services;

import io.github.abdulwahabo.filebox.exceptions.FileDeleteException;
import io.github.abdulwahabo.filebox.exceptions.FileDownloadException;
import io.github.abdulwahabo.filebox.exceptions.FileUploadException;
import java.util.Optional;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class AwsS3Client {

    private Logger logger = LoggerFactory.getLogger(AwsS3Client.class);

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.dynamo.region}")
    private String region;

    private S3Client s3Client;

    /**
     *
     */
    public boolean uploadFile(String key, byte[] bytes) throws FileUploadException {
        PutObjectRequest request = PutObjectRequest.builder()
                                                   .key(key)
                                                   .bucket(bucket)
                                                   .build();

        try {
            PutObjectResponse response = s3Client.putObject(request, RequestBody.fromBytes(bytes));
            SdkHttpResponse httpResponse = response.sdkHttpResponse();

            if (!httpResponse.isSuccessful()) {
                logger.error("Failed to upload file with key: " + key + " [" + httpResponse.statusCode() + "]");
                return false;
            } else {
                logger.info("Upload successful for file with key: " + key);
                return true;
            }
        } catch (S3Exception e) {
            throw new FileUploadException("File upload failed", e);
        }
    }

    /**
     *
     */
    public Optional<byte[]> downloadFile(String key) throws FileDownloadException {
        GetObjectRequest request = GetObjectRequest.builder()
                                                   .bucket(bucket)
                                                   .key(key)
                                                   .build();

        try {
            ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(request);
            SdkHttpResponse httpResponse = responseBytes.response().sdkHttpResponse();

            if (httpResponse.isSuccessful()) {
                logger.info("Downloaded file with key: " + key);
                return Optional.of(responseBytes.asByteArray());
            } else {
                logger.error("Failed to download file with key: " + key + " [" + httpResponse.statusCode() + "]");
                return Optional.empty();
            }
        } catch (S3Exception e) {
            throw new FileDownloadException("Failed to download file", e);
        }
    }

    /**
     *
     */
    public boolean deleteFile(String key) throws FileDeleteException {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                                                                     .bucket(bucket)
                                                                     .key(key)
                                                                     .build();

        try {
            DeleteObjectResponse response = s3Client.deleteObject(deleteObjectRequest);
            SdkHttpResponse httpResponse = response.sdkHttpResponse();

            if (httpResponse.isSuccessful()) {
                logger.info("Deleted file with key: " + key);
                return true;
            } else {
                logger.error("Failed to delete file with key: " + key + " [" + httpResponse.statusCode() + "]");
                return false;
            }
        } catch (S3Exception e) {
            throw new FileDeleteException("Failed to delete file with key: " + key, e);
        }
    }

    @PostConstruct
    private void initClient() {
        Region reg = Region.of(region);
        s3Client = S3Client.builder()
                           .region(reg)
                           .build();
    }
}
