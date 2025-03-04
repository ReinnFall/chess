package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import service.RegisterService;
import spark.*;
import service.LoginService;
import model.UserData;

public class Server {
    private final LoginService loginService;
    private final RegisterService registerService;
    private final UserDAO userDAO; // Potential optimization
    private final AuthDAO authDAO;

    public Server(){
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        loginService = new LoginService(userDAO,authDAO);
        registerService = new RegisterService(userDAO,authDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/session", this::loginHandler);
        Spark.post("/user",this::registerHandler);

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
        var userLoginInfo = new Gson().fromJson(req.body(),UserData.class);
        AuthData loginResult = loginService.loginRequest(userLoginInfo);

        res.status(200);
        res.body(new Gson().toJson(loginResult));

        return res.body();
    }
    private Object registerHandler(Request req, Response res) throws DataAccessException {
        var userLoginInfo = new Gson().fromJson(req.body(),UserData.class);
        AuthData registerResult = registerService.registerRequest(userLoginInfo);

        res.status(200);
        res.body(new Gson().toJson(registerResult));

        return res.body();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
