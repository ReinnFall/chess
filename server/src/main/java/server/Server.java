package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import service.*;
import spark.*;
import model.UserData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class Server {
    private final LoginService loginService;
    private final RegisterService registerService;
    private final LogoutService logoutService;
    private final ClearService clearService;
    private final CreateGameService createGameService;
    private final ListGameService listGameService;
    private final JoinGameService joinGameService;
    private  UserDAO userDAO; // Potential optimization
    private  AuthDAO authDAO;
    private  GameDAO gameDAO;

    public Server() {
        try{
            userDAO = new MySqlUserDAO();
            authDAO = new MySqlAuthDAO();
            gameDAO = new MySqlGameDAO();
        } catch (Exception ex){
            userDAO = new MemoryUserDAO();
            authDAO = new MemoryAuthDAO();
            gameDAO = new MemoryGameDAO();
        }

        loginService = new LoginService(userDAO,authDAO);
        registerService = new RegisterService(userDAO,authDAO);
        logoutService = new LogoutService(authDAO);
        clearService = new ClearService(userDAO,authDAO,gameDAO);
        createGameService = new CreateGameService(authDAO,gameDAO);
        listGameService = new ListGameService(authDAO,gameDAO);
        joinGameService = new JoinGameService(authDAO,gameDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/session", this::loginHandler);
        Spark.post("/user",this::registerHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.delete("/db", this::clearHandler);
        Spark.post("game",this::createGameHandler);
        Spark.get("/game",this::listGameHandler);
        Spark.put("/game", this::joinGameHandler);


        Spark.exception(DataAccessException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }
    private void exceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(ex.statusCode());
        res.body(ex.toJsonCustom());
    }
    private Object loginHandler(Request req, Response res) throws DataAccessException {
        UserData userLoginInfo = new Gson().fromJson(req.body(),UserData.class);
        AuthData loginResult = loginService.loginRequest(userLoginInfo);

        //res.status(200);
        res.body(new Gson().toJson(loginResult));

        return res.body();
    }
    private Object registerHandler(Request req, Response res) throws DataAccessException {
        UserData userRegisterInfo = new Gson().fromJson(req.body(),UserData.class);
        AuthData registerResult = registerService.registerRequest(userRegisterInfo);

        //res.status(200);
        res.body(new Gson().toJson(registerResult));

        return res.body();
    }
    private Object logoutHandler(Request req, Response res) throws DataAccessException, SQLException {
        String authToken = req.headers("authorization");
        logoutService.logoutRequest(authToken);
        //res.status(200);

        return "";
    }
    private Object clearHandler(Request req, Response res) throws DataAccessException{
        clearService.clearAllData();

        return "";
    }
    private Object createGameHandler(Request req, Response res) throws DataAccessException {
        GameData gameName = new Gson().fromJson(req.body(), GameData.class);
        String authToken = req.headers("authorization");

        int gameId = createGameService.createGameRequest(gameName,authToken);
        Map<String, Integer> responseMap = Map.of("gameID",gameId);

        res.body(new Gson().toJson(responseMap));

        return res.body();
    }
    private Object listGameHandler(Request req, Response res) throws DataAccessException{
        String authToken = req.headers("authorization");
        Collection<GameData> games = listGameService.listGameRequest(authToken);
        Map<String, Collection<GameData>> responseMap = Map.of("games",games);

        res.body(new Gson().toJson(responseMap));

        return res.body();
    }
    private Object joinGameHandler(Request req, Response res) throws DataAccessException, SQLException {
        String authToken = req.headers("authorization");
        JoinGameData playerColorAndID = new Gson().fromJson(req.body(), JoinGameData.class);

        joinGameService.joinGameRequest(playerColorAndID,authToken);
        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
