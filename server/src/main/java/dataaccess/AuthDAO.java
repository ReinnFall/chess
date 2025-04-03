package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO{
    public void createAuth(AuthData data) throws DataAccessException;
    public AuthData getAuth(String token) throws DataAccessException;
    public void deleteAuth(AuthData data) throws DataAccessException, SQLException;
    public void clearAuthData() throws DataAccessException;
    public AuthData getAuthByUsername(String username);

}