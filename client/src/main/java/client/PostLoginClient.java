package client;

import exception.ResponseException;

import java.util.Arrays;

public class PostLoginClient implements ClientState{
    private final ServerFacade server;
    private State state = State.SIGNEDIN;

    public PostLoginClient(ServerFacade server)  {
        this.server = server;
    }

    @Override
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
                case "logout" -> logout();
                case "create game" -> createGame(params);
                case "list games" -> listGames();
                case "play game" -> playGame();
                case "observe game" -> observGame();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String observGame() {
        return "";
    }

    private String playGame() {
        return "";
    }

    private String listGames() {
        return "";
    }

    private String createGame(String[] params) {
        return "";
    }

    private String logout() throws  ResponseException {
        server.logout();
        return "logout";
    }

    @Override
    public String help() {
        return "hello world";
    }

    @Override
    public String printPrompt() {
        String status;
        if(state == State.SIGNEDOUT){
            status = "[LOGGED OUT] ";
        } else{
            status = "[LOGGED IN] ";
        }
        return ("\n" + status + ">>> " );
    }
}