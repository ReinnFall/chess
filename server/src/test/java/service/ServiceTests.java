package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

class ServiceTests {
    private UserData userInput;
    private UserData userInput2;
    private GameData gameData;
    private GameData gameData2;
    private LoginService loginService;
    private RegisterService registerService;
    private LogoutService logoutService;
    private CreateGameService createGameService;
    private ListGameService listGameService;
    private JoinGameService joinGameService;
    private ClearService clearService;

    @BeforeEach
    public void beforeEach(){
        userInput = new UserData("James","Stock","js@mail");
        userInput2 = new UserData("Jacob","Stock","js@mail");
        gameData = new GameData(0,null, null,"Losers",null);
        gameData2 = new GameData(0,null, null,"Winners",null);
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        loginService = new LoginService(userDAO,authDAO);
        registerService = new RegisterService(userDAO,authDAO);
        logoutService = new LogoutService(authDAO);
        createGameService = new CreateGameService(authDAO,gameDAO);
        listGameService = new ListGameService(authDAO, gameDAO);
        joinGameService = new JoinGameService(authDAO,gameDAO);
        clearService = new ClearService(userDAO,authDAO,gameDAO);

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
    public void logoutNeedsCorrectAuthData() throws DataAccessException{
        String badToken = "123";
        registerService.registerRequest(userInput);

        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () ->{
            logoutService.logoutRequest(badToken);
        });
    }
    @Test
    public void logoutSuccessfullyDeletesAuthData() throws DataAccessException, SQLException {
        registerService.registerRequest(userInput);
        AuthData authData = logoutService.getAuthData("James");
        logoutService.logoutRequest(authData.authToken());

        //Attempting to use old AuthToken
        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () ->{
            listGameService.listGameRequest(authData.authToken());
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

    @Test
    public void listGamesRequiresCorrectAuth() throws DataAccessException{
        registerService.registerRequest(userInput);
        String badToken = "123";

        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () ->{
            listGameService.listGameRequest(badToken);
        });
    }
    @Test
    public void listGamesWorksCorrectly() throws DataAccessException{
        registerService.registerRequest(userInput);
        AuthData authData = createGameService.getAuthData(userInput.username());

        createGameService.createGameRequest(gameData,authData.authToken());
        GameData expectedData = new GameData(1,null,null,"Losers",null);

        Collection<GameData> expected = new HashSet<>();
        expected.add(expectedData);
        Collection<GameData> actual = listGameService.listGameRequest(authData.authToken());

        Assertions.assertEquals(expected,actual);
    }
    @Test
    public void joinGameRequiresCorrectID() throws DataAccessException{
        JoinGameData badID = new JoinGameData("WHITE",500);
        registerService.registerRequest(userInput);
        AuthData authData = createGameService.getAuthData(userInput.username());
        createGameService.createGameRequest(gameData,authData.authToken());

        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () ->{
            joinGameService.joinGameRequest(badID, authData.authToken());
        });
    }
    @Test
    public void joinGameSuccessfullyAddsUser() throws DataAccessException, SQLException {
        registerService.registerRequest(userInput);
        AuthData authData = createGameService.getAuthData(userInput.username());
        createGameService.createGameRequest(gameData,authData.authToken());

        JoinGameData goodData = new JoinGameData("WHITE",1);
        joinGameService.joinGameRequest(goodData,authData.authToken());

        GameData expectedData = new GameData(1,"James",null,"Losers",null);

        Collection<GameData> expected = new HashSet<>();
        expected.add(expectedData);
        Collection<GameData> actual = listGameService.listGameRequest(authData.authToken());

        Assertions.assertEquals(expected,actual);
    }
    @Test
    public void clearDeletesUserData() throws DataAccessException{
        registerService.registerRequest(userInput);
        clearService.clearAllData();

        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () ->{
            loginService.loginRequest(userInput);
        });
    }
}