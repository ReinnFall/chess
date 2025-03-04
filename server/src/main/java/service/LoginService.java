package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataaccess.UserDAO;

import java.util.Objects;
import java.util.UUID;



public class LoginService{
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public AuthData LoginRequest(UserData data) throws DataAccessException {

        UserData dataFromMemory = userDAO.getUser(data.username());
        if (dataFromMemory == null){
            //return the error code message for username not found
            throw new DataAccessException("Error: username not found");
        }
        //assert dataFromMemory != null;
        if (!Objects.equals(data.password(), dataFromMemory.password())){ // Look into equals method
            //return error code message for unauthorized
            throw new DataAccessException("Error: unauthorized");
        }
        //Create random string and add it to local storage
        AuthData newAuthData = new AuthData(generateToken(), data.username());
        authDAO.createAuth(newAuthData);

        return newAuthData;
    }
}