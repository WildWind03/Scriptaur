package ru.nsu.fit.pm.scriptaur.auth;

import lombok.Value;

@Value
public class SignUpData {
    private String password;
    private String username;
    private String name;
}
