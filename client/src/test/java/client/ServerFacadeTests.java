package client;

import dataaccess.DataAccessException;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        String serverUrl = "http://localhost:" + port;
        facade = new ServerFacade(serverUrl);
    }

    @AfterAll
    static void stopServer() throws ResponseException {
        server.stop();
        //facade.clear();
    }


    @Test
    void registerPositive() throws Exception {
        UserData data = new UserData("James","Stock","js@mail");
        var authData = facade.register(data);
        assertTrue(authData.authToken().length() > 10);
    }
    @Test
    void registerNegative() throws Exception{
        UserData badData = new UserData("James","Stock",null);
        ResponseException ex = Assertions.assertThrows(ResponseException.class, () ->{
            var authData = facade.register(badData);
        });
    }
    @Test
    void loginPositive() throws Exception{
        UserData data = new UserData("James","Stock","js@mail");
        var authData = facade.register(data);

        facade.logout();
        UserData loginData = new UserData("James","Stock",null);
        AuthData auth = facade.login(loginData);

        assertEquals(data.username(),auth.username());
    }
    @Test
    void loginNegative() throws Exception{
        UserData data = new UserData("James","Stock","js@mail");
        ResponseException ex = Assertions.assertThrows(ResponseException.class, () ->{
            var authData = facade.login(data);
        });
    }
    @Test
    void logoutPositive() throws Exception{
        UserData data = new UserData("James","Stock","js@mail");
        var authData = facade.register(data);

        facade.logout();

        assertNull(facade.getAuth());
    }
    @Test
    void logoutNegative() throws Exception{
        UserData data = new UserData("James","Stock","js@mail");
        var authData = facade.register(data);
        facade.setAuth(null);

        ResponseException ex = Assertions.assertThrows(ResponseException.class, () ->{
            facade.logout();
        });
    }
    @Test
    void createGamePositive() throws Exception{
        UserData data = new UserData("James","Stock","js@mail");
        facade.register(data);

        String gameName = "Losers";
        GameData gameData = new GameData(0,null,null,gameName,null);
        facade.createGame(gameData);

        List<GameData> games = facade.listGames();
        assertNotNull(games);
    }
    @Test
    void createGameNegative() throws Exception{
        UserData data = new UserData("James","Stock","js@mail");
        facade.register(data);

        String gameName = "Losers";
        GameData badData = new GameData(0,null,null,null,null);
        ResponseException ex = Assertions.assertThrows(ResponseException.class, () ->{
            facade.createGame(badData);
        });
    }
    @Test
    void listGamesPositive() throws Exception{
        UserData data = new UserData("James","Stock","js@mail");
        facade.register(data);

        String gameName = "Losers";
        GameData gameData = new GameData(0,null,null,gameName,null);
        facade.createGame(gameData);

        assertNotNull(facade.listGames());
    }
    @Test
    void listGamesNegative() throws Exception{
        UserData data = new UserData("James","Stock","js@mail");
        facade.register(data);

        List<GameData> games = facade.listGames();
        assertTrue(games.isEmpty());
    }
    @Test
    void joinGamePositive() throws Exception{
        UserData data = new UserData("James","Stock","js@mail");
        facade.register(data);

        String gameName = "Losers";
        GameData gameData = new GameData(0,null,null,gameName,null);
        int gameId = facade.createGame(gameData);


        facade.joinGame(gameId,"WHITE");
        List<GameData> games = facade.listGames();

        assertEquals("James",games.getFirst().whiteUsername());
    }
    @Test
    void joinGameNegative() throws Exception{
        UserData data = new UserData("James","Stock","js@mail");
        facade.register(data);

        ResponseException ex = Assertions.assertThrows(ResponseException.class, () ->{
            facade.joinGame(1,"WHITE");
        });

    }
}
