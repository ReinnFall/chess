package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataaccess.UserDAO;

import java.util.Objects;
import java.util.UUID;



public class RegisterService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public AuthData registerRequest(UserData data) throws DataAccessException {
        UserData dataFromMemory = userDAO.getUser(data.username());
        if (dataFromMemory != null) {
            throw new DataAccessException(403,"Error: already taken");
        }
        else {
            if(data.username() == null || data.password() == null || data.email() == null){
                throw new DataAccessException(400, "Error: bad request");
            }
            else{
                userDAO.createUser(data);
                AuthData newAuthData = new AuthData(generateToken(), data.username());
                authDAO.createAuth(newAuthData);
                return newAuthData;
            }

        }
    }
}