package client;

import exception.ResponseException;

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
                case "Help" -> help();
                case "Login" -> login(params);
                case "Register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    public String help(){
        return "All text needed for help";
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
        return "Successfully registered";
    }


}