package client;

import dataaccess.DataAccessException;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

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
}
