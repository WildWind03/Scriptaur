package ru.nsu.fit.pm.scriptaur.service;

import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService{
    @Override
    public int getUserIdByToken(String token) {
        return 1;
    }

    @Override
    public boolean checkTokenValidity(String token) {
        return true;
    }
}
