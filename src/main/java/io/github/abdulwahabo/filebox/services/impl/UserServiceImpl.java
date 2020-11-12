package io.github.abdulwahabo.filebox.services.impl;

import io.github.abdulwahabo.filebox.exceptions.FileUploadException;
import io.github.abdulwahabo.filebox.model.File;
import io.github.abdulwahabo.filebox.model.User;
import io.github.abdulwahabo.filebox.services.DynamoDBClient;
import io.github.abdulwahabo.filebox.services.FileStorageService;
import io.github.abdulwahabo.filebox.services.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {

    private FileStorageService fileStorageService;
    private DynamoDBClient dynamoDBClient;

    @Autowired
    public UserServiceImpl(FileStorageService fileStorageService, DynamoDBClient dynamoDBClient) {
        this.fileStorageService = fileStorageService;
        this.dynamoDBClient = dynamoDBClient;
    }

    @Override
    public User get(String email) {

        // todo: go to DynamoDB and fetch user...
        return null;
    }

    @Override
    public User save(User user) {

        /// Save user to DynamoDB.
        return null;
    }

    @Override
    public User addFile(MultipartFile multipartFile, String email) throws FileUploadException  {

        User user = get(email);
        List<File> files = user.getFiles();
        LocalDateTime time = LocalDateTime.now();

        File file = new File();
        file.setName(multipartFile.getOriginalFilename());
        file.setSize((double) multipartFile.getSize() / 1000);
        file.setUploadDate(time);
        file.setUploadDateString(time.format(DateTimeFormatter.RFC_1123_DATE_TIME));

        try {
            String id = fileStorageService.upload(multipartFile.getBytes());
            file.setStorageID(""); // todo
            files.add(file);
            user.setFiles(files);

            // TODO: save user to DynamoDB
            return user;

        } catch (IOException e) {
            throw new FileUploadException("Failed to add file for user " + user.getEmail(), e);
        }
    }
}
