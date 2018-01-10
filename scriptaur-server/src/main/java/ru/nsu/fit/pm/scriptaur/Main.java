package ru.nsu.fit.pm.scriptaur;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.nsu.fit.pm.scriptaur.auth.SignUpData;
import ru.nsu.fit.pm.scriptaur.dao.UserDao;
import ru.nsu.fit.pm.scriptaur.entity.User;

import javax.jws.soap.SOAPBinding;
import java.util.List;


@SpringBootApplication
//@ComponentScan("ru.nsu.fit.pm.scriptaur.controller")
public class Main  {

    @Autowired
    ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }
}
