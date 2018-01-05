package ru.nsu.fit.pm.scriptaur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.fit.pm.scriptaur.dao.NoEntityException;
import ru.nsu.fit.pm.scriptaur.dao.TokenIdMatherDao;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenIdMatherDao tokenIdMatherDao;

    @Override
    public int getUserIdByToken(String token) throws NoEntityException {
        return tokenIdMatherDao.getId(token);
    }

    @Override
    public boolean checkTokenValidity(String token) {
        try {
            getUserIdByToken(token);
            return true;
        } catch (NoEntityException e) {
            return false;
        }
    }

    @Override
    public void deleteUser(String token) {
        tokenIdMatherDao.delete(token);
    }

    @Override
    public void addTokenId(int id, String token) {
        tokenIdMatherDao.addNew(id, token);
    }

}
