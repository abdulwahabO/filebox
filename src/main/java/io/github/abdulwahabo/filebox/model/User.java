package io.github.abdulwahabo.filebox.model;

import java.util.ArrayList;
import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/**
 *
 */
@DynamoDbBean
public class User {

    private String email;
    private String name;
    private List<File> files = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<File> getFiles() {
        return new ArrayList<>(files);
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @DynamoDbPartitionKey
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
