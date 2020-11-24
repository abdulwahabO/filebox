package io.github.abdulwahabo.filebox.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.abdulwahabo.filebox.model.File;
import io.github.abdulwahabo.filebox.model.User;
import io.github.abdulwahabo.filebox.services.DynamoDBClient;
import io.github.abdulwahabo.filebox.services.FileStorageService;

import io.github.abdulwahabo.filebox.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

public class UserServiceImplTest {

    private FileStorageService fileStorageService = mock(FileStorageService.class);
    private DynamoDBClient dynamoDBClient = mock(DynamoDBClient.class);
    private User mockUser = new User();
    private String mockEmail = "abc@124.com";
    private String mockName = "ade shina";
    private MultipartFile file = mock(MultipartFile.class);
    private String mockFileName = "Mock_file.jpg";
    private long mockFileSize = 2321;
    private String mockFileID = "mock_file_id";
    private byte[] mockFileBytes = new byte[5];

    @BeforeEach
    public void setup() throws Exception {
        mockUser.setName(mockName);
        mockUser.setEmail(mockEmail);
        when(dynamoDBClient.getUser(mockEmail)).thenReturn(mockUser);
        doNothing().when(dynamoDBClient).saveUser(mockUser);
        when(file.getBytes()).thenReturn(mockFileBytes);
        when(file.getOriginalFilename()).thenReturn(mockFileName);
        when(file.getSize()).thenReturn(mockFileSize);
        when(fileStorageService.upload(any(), anyString())).thenReturn(mockFileID);
    }

    @Test
    public void shouldAddFileForUser() throws Exception {
        UserService userService = new UserServiceImpl(fileStorageService, dynamoDBClient);
        User user = userService.addFile(file, mockEmail);
        File file = user.getFiles().get(0);
        assertEquals(mockFileName, file.getName());
        assertEquals((double) mockFileSize / 1000, file.getSize());
        assertEquals(mockFileID, file.getStorageID());
        verify(fileStorageService).upload(mockFileBytes, mockEmail);
    }
}
