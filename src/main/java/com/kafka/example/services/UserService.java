package com.kafka.example.services;

import com.kafka.example.models.User;

import java.util.List;


public interface UserService {
    List<User> getAllUser();
    User addUser(User user);
}
