package ru.nsu.fit.pm.scriptaur.entity;

public class SignUpData {
    String username;
    String password;

    @Override
    public String toString() {
        return "SignUpData{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
