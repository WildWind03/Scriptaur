package ru.nsu.fit.pm.scriptaur.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.nsu.fit.pm.scriptaur.dao.UserDao;
import ru.nsu.fit.pm.scriptaur.entity.User;
import ru.nsu.fit.pm.scriptaur.service.UserService;
import ru.nsu.fit.pm.scriptaur.service.UserServiceImpl;

import java.util.List;


@SpringBootApplication
@ComponentScan("ru.nsu.fit.pm.scriptaur")
public class Main implements CommandLineRunner {

    private final ApplicationContext context;

    @Autowired
    public Main(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        UserService userService = context.getBean(UserServiceImpl.class);
        userService.addUser(new User("Sasha", 200, "ghgh", "dfddg"));
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext
//                ("spring.xml");
//        UserDao userDao = context.getBean(UserDao.class);
//
//        User testUser = userDao.getUserById(1);
//        System.out.println(testUser.getUsername());
//
//        List<User> users = userDao.getAllUsers();
//        users.forEach(u -> System.out.println(u.getUsername() + " "));
//
//        List<User> _users = userDao.getAllUsers();
//        _users.forEach(u -> System.out.println(u.getUsername() + " "));

    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
