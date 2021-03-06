package io.github.abdulwahabo.filebox.services.impl;

import io.github.abdulwahabo.filebox.exceptions.AwsClientException;
import io.github.abdulwahabo.filebox.exceptions.FileUploadException;
import io.github.abdulwahabo.filebox.exceptions.UserCreateException;
import io.github.abdulwahabo.filebox.exceptions.UserNotFoundException;
import io.github.abdulwahabo.filebox.model.File;
import io.github.abdulwahabo.filebox.model.User;
import io.github.abdulwahabo.filebox.services.DynamoDBClient;
import io.github.abdulwahabo.filebox.services.FileStorageService;
import io.github.abdulwahabo.filebox.services.UserService;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Default implementation of {@link UserService} which uses DynamoDB to persist user data.
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);
    private FileStorageService fileStorageService;
    private DynamoDBClient dynamoDBClient;

    @Autowired
    public UserServiceImpl(FileStorageService fileStorageService, DynamoDBClient dynamoDBClient) {
        this.fileStorageService = fileStorageService;
        this.dynamoDBClient = dynamoDBClient;
    }

    @Override
    public User get(String email) throws UserNotFoundException {
        try {
            Optional<User> userOptional = dynamoDBClient.getUser(email);
            User user = userOptional.orElseThrow(() -> new UserNotFoundException("No user found for: " + email));
            logger.info("Retrieved new user with email " + user.getEmail());
            return user;
        } catch (AwsClientException e) {
            throw new UserNotFoundException("No user found with given email:" + email, e);
        }
    }

    @Override
    public User save(User user) throws UserCreateException {
        try {
            dynamoDBClient.saveUser(user);
            logger.info("Saved new user with email " + user.getEmail());
            return user;
        } catch (AwsClientException e) {
            throw new UserCreateException("Failed to save user with email: " + user.getEmail(), e);
        }
    }

    @Override
    public User addFile(MultipartFile multipartFile, String email) throws FileUploadException {
        try {
            User user = get(email);
            List<File> files = user.getFiles();
            ZonedDateTime time = ZonedDateTime.now(ZoneId.of("GMT+1"));

            File file = new File();
            file.setName(multipartFile.getOriginalFilename());
            file.setSize((double) multipartFile.getSize() / 1000);
            file.setUploadDate(time);
            file.setUploadDateString(time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM,
                    FormatStyle.LONG)));

            String id = fileStorageService.upload(multipartFile.getBytes(), user.getEmail());
            file.setStorageID(id);
            files.add(file);
            user.setFiles(files);
            save(user);
            return user;
        } catch (IOException | UserCreateException | UserNotFoundException e) {
            throw new FileUploadException("Failed to add file for a user", e);
        }
    }
}
