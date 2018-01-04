package ru.nsu.fit.pm.scriptaur.auth;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.pm.scriptaur.entity.User;
import ru.nsu.fit.pm.scriptaur.service.UserService;

import java.util.Date;

@RestController
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class.getName());
    private static final int DEFAULT_TRUST_FACTOR = 0;
    private static final String DEFAULT_SALT = "Russia";

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/signin", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity signIn(@RequestBody String data) {
        Gson gson = new Gson();
        SignInData signInData = gson.fromJson(data, SignInData.class);
        //userService.getUserById()
        return null;

    }

    @RequestMapping(path = "/signup",  method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity signUp(@RequestBody String data) {
        Gson gson = new Gson();
        SignUpData signUpData = gson.fromJson(data, SignUpData.class);

        String hashedPassword = passwordEncoder.encodePassword(signUpData.getPassword(), DEFAULT_SALT);
        userService.addUser(new User(signUpData.getUsername(), DEFAULT_TRUST_FACTOR,
                hashedPassword, DEFAULT_SALT));

        return new ResponseEntity<> (Jwts.builder()
                .setSubject(signUpData.getUsername())
                .setIssuedAt(new Date())
                .compact(), HttpStatus.OK);

    }
}
