package ru.nsu.fit.pm.scriptaur.controller;


import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.pm.scriptaur.auth.SignUpData;
import ru.nsu.fit.pm.scriptaur.dao.NoEntityException;
import ru.nsu.fit.pm.scriptaur.entity.User;
import ru.nsu.fit.pm.scriptaur.service.TokenService;
import ru.nsu.fit.pm.scriptaur.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/users")
@ComponentScan("ru.nsu.fit.pm.scriptaur.service")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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


    //example
    @RequestMapping(method = RequestMethod.GET, params = {"user_id", "token"})
    @ResponseBody
    public ResponseEntity get(@RequestParam(value = "user_id") int id, @RequestParam(value = "token") String token) {
        System.out.println("get one user");

        User user = userService.getUserById(id);
        System.out.println(user);

        if (user == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity get(@RequestParam(value = "token") String token) {

        if (!tokenService.checkTokenValidity(token)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        User user = null;
        try {
            user = userService.getUserById(tokenService.getUserIdByToken(token));
        } catch (NoEntityException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        if (user == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity putUser(@RequestParam(value = "token") String token, @RequestBody(required = true) String signUpJson) {
        if (!tokenService.checkTokenValidity(token)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        Gson gson = new Gson();
        SignUpData signUpData = gson.fromJson(signUpJson, SignUpData.class);
        System.out.println(signUpData);

        try {
            int id  = tokenService.getUserIdByToken(token);
            User user = userService.getUserById(id);
            user.setHash(passwordEncoder.encodePassword(signUpData.getPassword(), user.getSalt()));
            userService.updateUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (NoEntityException e) {
            LOGGER.error("Invalid args. Can't find a user");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
