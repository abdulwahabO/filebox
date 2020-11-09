package io.github.abdulwahabo.filebox.services;

import io.github.abdulwahabo.filebox.model.User;

public interface UserService {

    /**
     *
     * @param email
     * @return
     */
    User get(String email);
}
