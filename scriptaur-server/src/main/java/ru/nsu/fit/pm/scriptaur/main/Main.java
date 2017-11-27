package ru.nsu.fit.pm.scriptaur.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.nsu.fit.pm.scriptaur.dao.UserDao;
import ru.nsu.fit.pm.scriptaur.entity.User;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext
                ("spring.xml");
        UserDao userDao = context.getBean(UserDao.class);

        User testUser = userDao.getUserById(1);
        System.out.println(testUser.getUsername());

        List<User> users = userDao.getAllUsers();
        users.forEach(u -> System.out.println(u.getUsername() + " "));

        List<User> _users = userDao.getAllUsers();
        _users.forEach(u -> System.out.println(u.getUsername() + " "));

    }
}
