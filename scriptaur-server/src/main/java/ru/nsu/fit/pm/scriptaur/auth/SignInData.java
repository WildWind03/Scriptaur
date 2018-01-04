package ru.nsu.fit.pm.scriptaur.auth;

import lombok.Value;

@Value
class SignInData {
    String username;
    String password;
}
