package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoginService;

import java.util.Objects;

class LoginServiceTest{
    private UserData userInput;
    private String expectedAuthUsername;
    private LoginService loginService;
    private RegisterService registerService;

    @BeforeEach
    public void beforeEach(){

        userInput = new UserData("James","Stock",null);
        expectedAuthUsername = "James";
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        loginService = new LoginService(userDAO,authDAO);
        registerService = new RegisterService(userDAO,authDAO);

    }

    @Test
    public void authTokenUsernameCheck() throws DataAccessException {
        registerService.registerRequest(userInput);
        AuthData actual = loginService.loginRequest(userInput);

        Assertions.assertEquals(expectedAuthUsername,actual.username());

    }
}