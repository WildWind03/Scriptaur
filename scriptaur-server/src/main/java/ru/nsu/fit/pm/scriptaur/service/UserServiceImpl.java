package ru.nsu.fit.pm.scriptaur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.pm.scriptaur.dao.UserDao;
import ru.nsu.fit.pm.scriptaur.entity.User;

import java.util.List;

@Service
//@ComponentScan("ru.nsu.fit.pm.scriptaur.dao")
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    @Transactional
    public void addUser(User user) {
        this.userDao.addUser(user);
    }

    @Transactional
    public void updateUser(User user) {
        this.userDao.updateUser(user);
    }

    @Transactional
    public List<User> getAllUsers() {
        return this.userDao.getAllUsers();
    }

    @Transactional
    public User getUserById(int id) {
        return this.userDao.getUserById(id);
    }

    @Transactional
    public void removeUser(int id) {
        this.userDao.removeUser(id);
    }

    @Override
    public void updateTrustFactor(int userId, float trustFactor) {
        this.userDao.updateTtustFactor(userId, trustFactor);
    }
}
