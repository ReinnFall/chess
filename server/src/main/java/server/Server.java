package server;

import com.google.gson.Gson;
import spark.*;
import service.LoginService;
import model.UserData;

public class Server {
    private final LoginService service;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/session", this::LoginHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }
    private Object LoginHandler(Request req, Response res){
        var userLoginInfo = new Gson().fromJson(req.body(),UserData.class);
        userLoginInfo = service.LoginRequest(userLoginInfo);

        return new Gson().toJson(userLoginInfo);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
