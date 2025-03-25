package dataaccess;

import model.UserData;

public class MySqlUserDAO implements UserDAO{

    private final String[] createStatements = {
            """
            
            """
    };

    public MySqlUserDAO() throws DataAccessException{
        ConfigureDatabase.configureDatabase(createStatements);
    }

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
}