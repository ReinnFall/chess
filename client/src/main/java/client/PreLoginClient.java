package client;

import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.util.Arrays;

public class PreLoginClient implements ClientState{
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public PreLoginClient(String serverUrl)  {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }
    public String eval(String input) {
        try {
            // Converts input into lowercase, splits it by spaces, and put into an array
            var tokens = input.toLowerCase().split(" ");
            //  If at least one token - assign to cmd/ if not call help
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            // Stores everything after cmd into an array
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> quit();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    public String help(){
        return """
        Options:
        Login as an existing user:  "login" <USERNAME> <PASSWORD>
        Register as a new user:     "register" <USERNAME> <PASSWORD> <EMAIL>
        Exit the program:           "quit"
        Print this message:         "help"
        """;
    }
    public String login(String... params) throws ResponseException {
        if (params.length != 3){
            throw new ResponseException(401,"Bad input - try again");
        }
        // Transition to PostLoginClient
        //server.login(params[0],params[1],params[2]);
        state = State.SIGNEDIN;
        return "Successfully logged in";
    }
    public String register(String... params) throws ResponseException {
        if (params.length != 3) {
            throw new ResponseException(401, "Bad input - try again");
        }
        UserData userData = new UserData(params[0],params[1],params[2]);
        AuthData authData = server.register(userData);
        return "Successfully registered";
    }
    public String quit(){
        return "quit";
    }

}