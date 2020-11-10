package io.github.abdulwahabo.filebox.services;

import io.github.abdulwahabo.filebox.exceptions.FileUploadException;
import io.github.abdulwahabo.filebox.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    /**
     *
     * @param email
     * @return
     */
    User get(String email);

    /**
     * Save user data to underlying datastore.
     *
     * @param user
     */
    User save(User user);

    /**
     *
     * @param file
     * @param email
     */
    User addFile(MultipartFile file, String email) throws FileUploadException;
}
