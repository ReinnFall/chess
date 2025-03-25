package dataaccess;

import model.AuthData;

public class MySqlAuthDAO implements AuthDAO{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS AuthData (
                authToken varchar(255) PRIMARY KEY,
                username varchar(255) NOT NULL,
                FOREIGN KEY (username) references UserData(username)
            )
            """
    };

    public MySqlAuthDAO() throws DataAccessException{
        ConfigureDatabase.configureDatabase(createStatements);
    }

    @Override
    public void createAuth(AuthData data) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData data) throws DataAccessException {

    }

    @Override
    public void clearAuthData() {

    }

    @Override
    public AuthData getAuthByUsername(String username) {
        return null;
    }

}