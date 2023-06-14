package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.domain.User;
import com.ust.onlineBookStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> checkLogin(String username, String password) {
        return  userRepository.findByUsernameAndPassword(username,password);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public long findCount() {
        return userRepository.count();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
