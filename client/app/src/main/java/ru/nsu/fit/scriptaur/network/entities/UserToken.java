package ru.nsu.fit.scriptaur.network.entities;

public class UserToken {
    private String token;

    public UserToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
