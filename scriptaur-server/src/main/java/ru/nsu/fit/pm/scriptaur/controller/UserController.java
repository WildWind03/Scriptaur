package ru.nsu.fit.pm.scriptaur.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.pm.scriptaur.dao.UserDaoImpl;
import ru.nsu.fit.pm.scriptaur.entity.User;
import ru.nsu.fit.pm.scriptaur.service.UserService;
import ru.nsu.fit.pm.scriptaur.service.UserServiceImpl;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ApplicationContext context;

    @Autowired
    public UserController(ApplicationContext context) {
        this.context = context;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<User> get(@RequestParam(value = "id") int id) {
        UserService userService = context.getBean(UserServiceImpl.class);
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity getAll() {
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
