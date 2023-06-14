package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> checkLogin(String username, String password);

    Optional<User> findByUsername(String username);

    long findCount();

    User save(User user);
}
