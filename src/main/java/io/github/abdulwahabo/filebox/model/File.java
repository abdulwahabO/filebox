package io.github.abdulwahabo.filebox.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class File {

    private String name;
    private LocalDateTime uploadDate;
    private String uploadDateString;
    private double size; // in kilobytes
    private String storageURL;

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getStorageURL() {
        return storageURL;
    }

    public String getUploadDateString() {
        return uploadDateString;
    }

    public void setUploadDateString(String uploadDateString) {
        this.uploadDateString = uploadDateString;
    }

    public void setStorageURL(String storageURL) {
        this.storageURL = storageURL;
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
