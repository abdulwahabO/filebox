package io.github.abdulwahabo.filebox.services;

import io.github.abdulwahabo.filebox.exceptions.AwsClientException;
import io.github.abdulwahabo.filebox.model.User;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

/**
 *
 */
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
     * Hacky way of checking.
     *
     * @param email
     * @return
     * @throws AwsClientException
     */
    public boolean userExists(String email) throws AwsClientException {
        try {
            Key key = Key.builder().partitionValue(email).build();
            PageIterable<User> userPageIterable = userTable.query(QueryConditional.keyEqualTo(key));
            List<Page<User>> users = userPageIterable.stream().collect(Collectors.toList());
            return !users.isEmpty();
        } catch (DynamoDbException e) {
            throw new AwsClientException(e.getMessage(), e);
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
