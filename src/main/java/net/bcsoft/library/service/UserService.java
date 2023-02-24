package net.bcsoft.library.service;

import net.bcsoft.library.model.User;

import java.util.List;

public interface UserService {

    void saveUser(User user);
    User readUserById(Long id);
    List<User> readAllUsers();
    void updateUser(User user);
    void deleteUser(Long id);

}
