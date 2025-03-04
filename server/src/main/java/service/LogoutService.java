package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class LogoutService {
    private AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO){
        this.authDAO = authDAO;
    }
    public void logoutRequest(String token) throws DataAccessException{
        AuthData authFromMemory = authDAO.getAuth(token);
        if(authFromMemory == null){
            throw new DataAccessException(401,"Error: unauthorized");
        }
        else{
            authDAO.deleteAuth(authFromMemory);
        }

    }
}