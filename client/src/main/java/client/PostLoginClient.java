package client;

import exception.ResponseException;
import model.GameData;

import java.util.Arrays;
import java.util.List;

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
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "play" -> playGame();
                case "watch" -> observGame();
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

    private String listGames() throws ResponseException {
        List<GameData> allGames = server.listGames();
        if (allGames.isEmpty()){
            return "No games found.";
        }
        StringBuilder combinedString = new StringBuilder();
        int startingID = 1;

        for (GameData game: allGames){
            //Game Number
            combinedString.append(startingID);
            startingID++;
            //Game Name
            combinedString.append(". Game name : ");
            combinedString.append(game.gameName());
            //White Player
            combinedString.append("\t" + "White: ");
            if(game.whiteUsername() == null){
                combinedString.append("empty");
            }else{
                combinedString.append(game.whiteUsername());
            }
            //Black Player
            combinedString.append("\t" + "Black: ");
            if(game.blackUsername() == null){
                combinedString.append("empty");
            }else{
                combinedString.append(game.whiteUsername());
            }
            combinedString.append("\n");
        }

        return combinedString.toString();
    }

    private String createGame(String... params) throws ResponseException {
        if (params.length != 1){
            throw new ResponseException(400, "Invalid input");
        }
        String gameName = params[0];
        GameData gameData = new GameData(0,null,null,gameName,null);

        int gameID = server.createGame(gameData);

        return "Successfully created game " + gameName;
    }

    private String logout() throws  ResponseException {
        server.logout();
        return "logout";
    }

    @Override
    public String help() {
        return """
        Options:
        List current games: "list"
        Create a new game:  "create" <GAME NAME>
        Join a game:        "join" <GAME ID> <COLOR>
        Watch a game:       "watch" <GAME ID>
        Logout:             "logout"
        """;
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