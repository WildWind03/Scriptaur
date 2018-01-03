package ru.nsu.fit.pm.scriptaur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.pm.scriptaur.dao.UserDao;
import ru.nsu.fit.pm.scriptaur.entity.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Bean
    public static UserServiceImpl getUserDaoImpl() {
        return new UserServiceImpl();
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        this.userDao.addUser(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        this.userDao.updateUser(user);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return this.userDao.getAllUsers();
    }

    @Override
    @Transactional
    public User getUserById(int id) {
        return this.userDao.getUserById(id);
    }

    @Override
    @Transactional
    public void removeUser(int id) {
        this.userDao.removeUser(id);
    }
}
