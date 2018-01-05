package ru.nsu.fit.pm.scriptaur.dao;

public interface TokenIdMatherDao {
    int getId(String token) throws NoEntityException;

    void addNew(int id, String token);

    void delete(String token);
}
