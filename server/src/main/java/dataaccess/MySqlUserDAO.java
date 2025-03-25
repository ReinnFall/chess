package dataaccess;

import model.UserData;

public class MySqlUserDAO implements UserDAO{

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {

    }

    @Override
    public void clearUserData() {

    }
    private final String[] createStatements = {

    };
}