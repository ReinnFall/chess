package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.UserData;
import dataaccess.UserDAO;



public class LoginService{
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = AuthDAO;
    }
    public UserData LoginRequest(UserData data) throws DataAccessException {
        /* Step 1 - Get userdata from db/local storage
        *  Step 2 - Check if userdata in db/local storage match
        *  Step 3 - Create authToken if they match and return username and authToken
        *  Step 4 - Throw exception if they don't match*/

        UserData dataFromMemory = userDAO.getUser(data.username());
        if (dataFromMemory == null){
            //return the error code message for username not found
        }
        if (data.password() != dataFromMemory.password()){ // Look into equals method
            //return error code message for unauthorized
        }


        return null;

    }
}