package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataaccess.UserDAO;
import org.mindrot.jbcrypt.BCrypt;

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
    public AuthData loginRequest(UserData data) throws DataAccessException {

        UserData dataFromStorage = userDAO.getUser(data.username());
        if (dataFromStorage == null){
            //return the error code message for username not found
            throw new DataAccessException(401,"Error: username not found");
        }
        //assert dataFromMemory != null;
        if (!BCrypt.checkpw(data.password(), dataFromStorage.password())){ // Look into equals method
            //return error code message for unauthorized
            throw new DataAccessException(401,"Error: unauthorized");
        }
        //Create random string and add it to local storage
        AuthData newAuthData = new AuthData(generateToken(), data.username());
        authDAO.createAuth(newAuthData);

        return newAuthData;
    }
}