package ru.nsu.fit.pm.scriptaur.auth;

import lombok.Value;

@Value
public class SignUpData {
    String password;
    String username;
    String name;
}
