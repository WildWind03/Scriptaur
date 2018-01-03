package ru.nsu.fit.pm.scriptaur.service;


import ru.nsu.fit.pm.scriptaur.entity.User;

import java.util.List;

public interface UserService {
    void addUser(User user);

    void updateUser(User user);

    List<User> getAllUsers();

    User getUserById(int id);

    void removeUser(int id);

    void updateTrustFactor(int userId, float trustFactor);
}
