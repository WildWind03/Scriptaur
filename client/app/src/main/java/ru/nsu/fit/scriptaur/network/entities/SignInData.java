package ru.nsu.fit.scriptaur.network.entities;

public class SignInData {
    private String username;
    private String password;

    public SignInData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
