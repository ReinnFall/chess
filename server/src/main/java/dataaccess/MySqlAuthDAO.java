package dataaccess;

import model.AuthData;

public class MySqlAuthDAO implements AuthDAO{

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

    private final String[] createStatements = {

    };

}