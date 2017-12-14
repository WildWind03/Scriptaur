package ru.nsu.fit.pm.scriptaur.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity get(@RequestParam(value = "id") int id) {
        System.out.println("one");
        return new ResponseEntity(HttpStatus.CHECKPOINT);
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAll() {
        System.out.println("here!");
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
