package io.github.abdulwahabo.filebox.model;

import java.time.ZonedDateTime;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

/**
 * Metadata for a file uploaded to Filebox. Objects of this class are persisted within the {@link User} that owns the
 * file.
 */
@DynamoDbBean
public class File {

    private String name;
    private ZonedDateTime uploadDate;
    private String uploadDateString;
    private double size;
    private String storageID;

    public ZonedDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(ZonedDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
     * Returns the unique key/ID for file on the cloud storage service where the file is persisted.
     */
    public String getStorageID() {
        return storageID;
    }

    /**
     * Returns the date when the file was uploaded, as a human-readable string.
     */
    public String getUploadDateString() {
        return uploadDateString;
    }

    public void setUploadDateString(String uploadDateString) {
        this.uploadDateString = uploadDateString;
    }

    public void setStorageID(String storageID) {
        this.storageID = storageID;
    }

    /**
     * Returns the size of the file in kilobytes.
     */
    public double getSize() {
        return size;
    }

    /**
     * Sets the size of the file. This has to be in kilobytes.
     * @param size The size of the file in kilobytes.
     */
    public void setSize(double size) {
        this.size = size;
    }

    /**
     * Returns the name of the file including it's file type extension if any.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
