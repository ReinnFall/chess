package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import service.ClearService;
import service.LogoutService;
import service.RegisterService;
import spark.*;
import service.LoginService;
import model.UserData;

public class Server {
    private final LoginService loginService;
    private final RegisterService registerService;
    private final LogoutService logoutService;
    private final ClearService clearService;
    private final UserDAO userDAO; // Potential optimization
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public Server(){
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        loginService = new LoginService(userDAO,authDAO);
        registerService = new RegisterService(userDAO,authDAO);
        logoutService = new LogoutService(authDAO);
        clearService = new ClearService(userDAO,authDAO,gameDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/session", this::loginHandler);
        Spark.post("/user",this::registerHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.delete("/db", this::clearHandler);


        Spark.exception(DataAccessException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }
    private void exceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(ex.StatusCode());
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
    private Object logoutHandler(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        logoutService.logoutRequest(authToken);
        //res.status(200);

        return "";
    }
    private Object clearHandler(Request req, Response res) throws DataAccessException{
        clearService.clearAllData();

        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
