package dataaccess;

import model.UserData;

import java.sql.SQLException;

public interface UserDAO{
    public UserData getUser(String username) throws DataAccessException;
    public void createUser(UserData data) throws DataAccessException;
    public void clearUserData();
}