package io.github.abdulwahabo.filebox.model;

import java.time.LocalDateTime;

public class File {

    private String name;
    private LocalDateTime uploadDate;
    private String uploadDateString;
    private double size; // in kilobytes
    private String storageID;

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getStorageID() {
        return storageID;
    }

    public String getUploadDateString() {
        return uploadDateString;
    }

    public void setUploadDateString(String uploadDateString) {
        this.uploadDateString = uploadDateString;
    }

    public void setStorageID(String storageID) {
        this.storageID = storageID;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
