package service;

import dataaccess.DataAccessException;
import model.UserData;
import dataaccess.UserDAO;



public class LoginService{
    private UserDAO userDAO;

    public LoginService(UserDAO userDAO){
        this.userDAO = userDAO;
    }
    public UserData LoginRequest(UserData data) throws DataAccessException {
        /* Step 1 - Get userdata from db/local storage
        *  Step 2 - Check if userdata in db/local storage match
        *  Step 3 - Create authToken if they match and return username and authToken
        *  Step 4 - Throw exception if they don't match*/

        userDAO.getUser(data.username());
        return null;

    }
}