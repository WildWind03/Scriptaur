package ru.nsu.fit.pm.scriptaur.service;


import ru.nsu.fit.pm.scriptaur.entity.User;

import java.util.Date;
import java.util.List;

public interface UserService {
    User addUser(User user);

    void updateUser(User user);

    List<User> getAllUsers();

    User getUserById(int id);

    User getUserByUsername(String username);

    void removeUser(int id);

    void updateTrustFactor(int userId, float trustFactor);

    float getUserTrustFactor(int userIdByToken);

    Date getDateOfTrustFactorUpdated(int userId);

    void updateTrustFactorDay(int userId);
}
