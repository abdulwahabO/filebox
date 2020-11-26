package io.github.abdulwahabo.filebox.services;

import io.github.abdulwahabo.filebox.exceptions.AwsClientException;
import io.github.abdulwahabo.filebox.exceptions.FileUploadException;
import io.github.abdulwahabo.filebox.exceptions.UserCreateException;
import io.github.abdulwahabo.filebox.exceptions.UserNotFoundException;
import io.github.abdulwahabo.filebox.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    /**
     * Returns the user matching the given email.
     *
     * @throws UserNotFoundException If no user was found.
     */
    User get(String email) throws UserNotFoundException;

   /* /**
     *
     * @param email
     * @return
     */
   /* boolean userExists(String email); */

    /**
     * Save the given user data to underlying datastore.
     *
     * @param user The user data
     */
    User save(User user) throws UserCreateException;

    /**
     * Adds a file to the collection of the user matching the given email.
     *
     * @param file The bytes of the file.
     * @param email The user's email.
     */
    User addFile(MultipartFile file, String email) throws FileUploadException;
}
