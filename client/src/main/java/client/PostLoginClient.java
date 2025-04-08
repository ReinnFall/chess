package client;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PostLoginClient implements ClientState{
    private final ServerFacade server;
    private State state = State.SIGNEDIN;
    private List<GameData> storedGames = new ArrayList<>();
    private TerminalChessBoard chessboard = new TerminalChessBoard();

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
                case "join" -> playGame(params);
                case "watch" -> observeGame();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String observeGame() {
        return "";
    }

    private String playGame(String... params) throws ResponseException {
        if (params.length != 2){
            return "Invalid input";
        }
        try {
            int requestedGameNumber = Integer.parseInt(params[0]);

            int numberOfGames = storedGames.size();
            if (requestedGameNumber <= 0 || requestedGameNumber > numberOfGames) {
                return "Invalid GameId";
            }
            String requestedTeamColor = params[1];
            if (!requestedTeamColor.equalsIgnoreCase("white") && !requestedTeamColor.equalsIgnoreCase("black")) {
                return "Wrong color. Please use either white or black";
            }
            int trueIndex = requestedGameNumber - 1;
            GameData selectedGame = storedGames.get(trueIndex);
            int trueGameID = selectedGame.gameID();

            requestedTeamColor = requestedTeamColor.toUpperCase();
            server.joinGame(trueGameID, requestedTeamColor);

            ChessGame currentChessGame = selectedGame.game();
            ChessGame.TeamColor playerTeam;
            if(requestedTeamColor.equalsIgnoreCase("white")){
                playerTeam = ChessGame.TeamColor.WHITE;
            } else{
                playerTeam = ChessGame.TeamColor.BLACK;
            }

            chessboard.printChessBoard(currentChessGame,playerTeam);

            return "Here's the board";
        } catch (Exception ex){
            return "Unable to join the game";
        }
    }

    private String listGames() throws ResponseException {
        List<GameData> allGames = server.listGames();
        storedGames = allGames;
        if (allGames == null){
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
                combinedString.append(game.blackUsername());
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
        try{
            server.logout();
            return "logout";
        } catch (ResponseException ex){
            return "Not authorized";
        }
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