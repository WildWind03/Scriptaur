package ru.nsu.fit.pm.scriptaur.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.pm.scriptaur.entity.SignUpData;
import ru.nsu.fit.pm.scriptaur.entity.User;
import ru.nsu.fit.pm.scriptaur.service.UserService;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/users")
@ComponentScan("ru.nsu.fit.pm.scriptaur.service")
public class UserController {

    @Autowired
    private UserService userService;


    // example
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAll() {
        System.out.println("get all users");

        // get all users

        List<User> users = userService.getAllUsers();

        if (users == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity get(@RequestParam(value = "user_id") int id, @RequestParam(value = "token") String token) {
        System.out.println("get one user");

        // find user by id
        User user = userService.getUserById(id);
        System.out.println(user);

        if (user == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity putUser(@RequestParam(value = "token") String token, @RequestBody(required = true) String signUpJson) {

        Gson gson = new Gson();
        SignUpData signUpData= gson.fromJson(signUpJson, SignUpData.class);
        System.out.println(signUpData);

        //toDo: process User with SignUpData

        User user = new User();

        if (user == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);


        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
