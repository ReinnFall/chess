package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceTests {
    private UserData userInput;
    private UserData userInput2;
    private GameData gameData;
    private GameData gameData2;
    private LoginService loginService;
    private RegisterService registerService;
    private LogoutService logoutService;
    private CreateGameService createGameService;

    @BeforeEach
    public void beforeEach(){
        userInput = new UserData("James","Stock","js@mail");
        userInput2 = new UserData("Jacob","Stock","js@mail");
        gameData = new GameData(0,null,
                null,"Losers",null);
        gameData2 = new GameData(0,null,
                null,"Winners",null);
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        loginService = new LoginService(userDAO,authDAO);
        registerService = new RegisterService(userDAO,authDAO);
        logoutService = new LogoutService(authDAO);
        createGameService = new CreateGameService(authDAO,gameDAO);

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

    @Test
    public void createGameCantHaveDuplicateNames() throws DataAccessException{
        registerService.registerRequest(userInput);
        AuthData authData = createGameService.getAuthData(userInput.username());
        createGameService.createGameRequest(gameData,authData.authToken());

        //Should throw erroe code 400 because it's using the name gameName
        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () ->{
            createGameService.createGameRequest(gameData,authData.authToken());
        });
    }
    @Test
    public void createGamesAssignsDifferentIDs() throws DataAccessException{
        registerService.registerRequest(userInput);
        AuthData authData = createGameService.getAuthData(userInput.username());
        int gameIDFirstGame = createGameService.createGameRequest(gameData,authData.authToken());
        int gameIDSecondGame = createGameService.createGameRequest(gameData2,authData.authToken());

        Assertions.assertNotEquals(gameIDFirstGame,gameIDSecondGame);

    }
}