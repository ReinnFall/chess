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
    private UserData userInput2;
    private LoginService loginService;
    private RegisterService registerService;
    private LogoutService logoutService;

    @BeforeEach
    public void beforeEach(){
        userInput = new UserData("James","Stock","js@mail");
        userInput2 = new UserData("Jacob","Stock","js@mail");
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        loginService = new LoginService(userDAO,authDAO);
        registerService = new RegisterService(userDAO,authDAO);
        logoutService = new LogoutService(authDAO);

    }

    @Test
    public void loginTokenCheckUsername() throws DataAccessException {
        String expectedAuthUsername = "James";
        registerService.registerRequest(userInput);
        AuthData actual = loginService.loginRequest(userInput);

        Assertions.assertEquals(expectedAuthUsername,actual.username());
    }
    @Test
    public void loginPasswordsDontMatch() throws DataAccessException{
        UserData badInput = new UserData("James","stock","js@mail");
        registerService.registerRequest(userInput);

        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () ->{
            loginService.loginRequest(badInput);
        });
    }
    @Test
    public void registerCheckForUniqueTokens() throws DataAccessException{
        AuthData authPerson1 = registerService.registerRequest(userInput);
        AuthData authPerson2 = registerService.registerRequest(userInput2);

        Assertions.assertNotEquals(authPerson1.authToken(),authPerson2.authToken());
    }
    @Test
    public void registerUsernameAlreadyTaken() throws DataAccessException{
        registerService.registerRequest(userInput);

        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () ->{
            registerService.registerRequest(userInput);
        });
    }
    @Test
    public void logoutDeletesAuthData() throws DataAccessException{
        String badToken = "123";
        registerService.registerRequest(userInput);

        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () ->{
            logoutService.logoutRequest(badToken);
        });
    }

}