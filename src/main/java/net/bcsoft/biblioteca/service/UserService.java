package net.bcsoft.biblioteca.service;

import net.bcsoft.biblioteca.model.User;

import java.util.List;

public interface UserService {

    void registerUser(User user);
    List<User> getAllUsers();

}
