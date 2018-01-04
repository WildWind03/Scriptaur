package ru.nsu.fit.pm.scriptaur.service;


public interface TokenService {
    int getUserIdByToken(String token);

    boolean checkTokenValidity(String token);
}
