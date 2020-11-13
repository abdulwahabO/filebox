package io.github.abdulwahabo.filebox.services;

import io.github.abdulwahabo.filebox.exceptions.AwsClientException;
import io.github.abdulwahabo.filebox.model.User;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@Service
public class DynamoDBClient {

    private DynamoDbTable<User> userTable;

    @Value("${aws.dynamo.user.table}")
    private String table;

    @Value("${aws.dynamo.region}")
    private String region;

    /**
     *
     */
    public User getUser(String email) throws AwsClientException {
        try {
            Key key = Key.builder().partitionValue(email).build();
            return userTable.getItem(key);
        } catch (DynamoDbException e) {
            throw new AwsClientException("Failed to get user with email:" + email, e);
        }
    }

    /**
     *
     */
    public void saveUser(User user) throws AwsClientException {
        try {
            userTable.putItem(user);
        } catch (DynamoDbException e) {
            throw new AwsClientException("Failed to save user with email: " + user.getEmail(), e);
        }
    }

    @PostConstruct
    private void initClient() {
        Region reg = Region.of(region);
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                                       .region(reg)
                                       .build();

        DynamoDbEnhancedClient dbEnhancedClient = DynamoDbEnhancedClient.builder()
                                                 .dynamoDbClient(dynamoDbClient)
                                                 .build();

        userTable = dbEnhancedClient.table(table, TableSchema.fromBean(User.class));
    }
}
