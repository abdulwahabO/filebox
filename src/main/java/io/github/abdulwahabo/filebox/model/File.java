package io.github.abdulwahabo.filebox.model;

import java.time.ZonedDateTime;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

/**
 *
 */
@DynamoDbBean
public class File {

    private String name;
    private ZonedDateTime uploadDate;
    private String uploadDateString;
    private double size; // in kilobytes
    private String storageID;

    public ZonedDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(ZonedDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getStorageID() {
        return storageID;
    }

    public String getUploadDateString() {
        return uploadDateString;
    }

    /**
     *
     * @param uploadDateString
     */
    public void setUploadDateString(String uploadDateString) {
        this.uploadDateString = uploadDateString;
    }

    public void setStorageID(String storageID) {
        this.storageID = storageID;
    }

    /**
     *
     * @return
     */
    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
