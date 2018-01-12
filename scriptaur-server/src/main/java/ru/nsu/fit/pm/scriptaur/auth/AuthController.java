package ru.nsu.fit.pm.scriptaur.auth;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.pm.scriptaur.entity.User;
import ru.nsu.fit.pm.scriptaur.service.TokenService;
import ru.nsu.fit.pm.scriptaur.service.UserService;

import java.util.Date;
import java.util.Random;

@RestController
public class AuthController {
    private static final int DEFAULT_TRUST_FACTOR = 1;
    private static final RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder().build();
    private static final int MIN_SALT_LENGTH = 5;
    private static final int MAX_SALT_LENGTH = 20;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/signin", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity signIn(@RequestBody String data) {
        Gson gson = new Gson();
        SignInData signInData = gson.fromJson(data, SignInData.class);
        User user = userService.getUserByUsername(signInData.getUsername());

        if (null == user) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            String hash = passwordEncoder.encodePassword(signInData.getPassword(), user.getSalt());
            if (hash.equals(user.getHash())) {

                String token = Jwts.builder()
                        .setSubject(user.getUsername())
                        .setIssuedAt(new Date())
                        .setId(Integer.toString(new Random(System.currentTimeMillis()).nextInt()))
                        .compact();

                tokenService.deleteUser(user.getUserId());
                tokenService.addTokenId(user.getUserId(), token);

                return new ResponseEntity<>(new TokenString(token), HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity signUp(@RequestBody String data) {
        Gson gson = new Gson();
        SignUpData signUpData = gson.fromJson(data, SignUpData.class);

        String salt = randomStringGenerator.generate(MIN_SALT_LENGTH, MAX_SALT_LENGTH);
        String hashedPassword = passwordEncoder.encodePassword(signUpData.getPassword(), salt);

        User user = new User(signUpData.getUsername(), DEFAULT_TRUST_FACTOR,
                hashedPassword, salt);
        userService.addUser(user);

        String token = Jwts.builder()
                .setSubject(signUpData.getUsername())
                .setIssuedAt(new Date())
                .setId(Integer.toString(new Random(System.currentTimeMillis()).nextInt()))
                .compact();

        tokenService.addTokenId(user.getUserId(), token);

        return new ResponseEntity<>(new TokenString(token), HttpStatus.OK);
    }

    @RequestMapping(path = "/signout", method = RequestMethod.GET, params = "token")
    @ResponseBody
    public ResponseEntity signOut(@RequestParam(value = "token") String token) {
        tokenService.deleteUser(token);
        return new ResponseEntity(HttpStatus.OK);
    }
}
