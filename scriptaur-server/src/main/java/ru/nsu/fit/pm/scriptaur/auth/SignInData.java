package ru.nsu.fit.pm.scriptaur.auth;

import lombok.Getter;
import lombok.Value;

@Value
@Getter
class SignInData {
    private String username;
    private String password;
}
