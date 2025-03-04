package dataaccess;

import model.AuthData;

public interface AuthDAO{
    public void createAuth(AuthData data) throws DataAccessException;
    public AuthData getAuth(String token) throws DataAccessException;
    public void deleteAuth(AuthData data) throws DataAccessException;
}