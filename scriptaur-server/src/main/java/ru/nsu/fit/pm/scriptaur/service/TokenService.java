package ru.nsu.fit.pm.scriptaur.service;


import ru.nsu.fit.pm.scriptaur.dao.NoEntityException;

public interface TokenService {
    int getUserIdByToken(String token) throws NoEntityException;

    boolean checkTokenValidity(String token);

    void deleteUser(String token);

    void addTokenId(int id, String token);

    void deleteUser(int userId);
}
