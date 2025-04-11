package client;

import chess.ChessGame;
import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;

import java.util.Arrays;

public class InGameClient implements ClientState{
    private final ServerFacade server;
    private State state = State.SIGNEDIN;
    private final ServerMessageHandler messageHandler;
    private WebSocketFacade ws;
    private final int gameID;
    private final ChessGame.TeamColor playerColor;
    private Position position;

    public InGameClient(ServerFacade server, ServerMessageHandler messageHandler, String serverUrl, int gameID, ChessGame.TeamColor playerColor, Position position)  {
        this.server = server;
        this.messageHandler = messageHandler;
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.position = position;
        try{
            ws = new WebSocketFacade(serverUrl,messageHandler);
            ws.connectToGame(server.getAuth(),gameID);

        } catch(ResponseException ex){
            System.out.println("Failed to connect to a game");
        }

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
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> makeMove(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String leave() throws ResponseException {
        ws.leaveGame(server.getAuth(),gameID);
        return "leave";
    }

    private String highlight(String[] params) {
        return "";
    }

    private String resign() {
        return "";
    }

    private String makeMove(String... params) {
        return "";
    }

    private String redraw() {
        return "";
    }

    @Override
    public String help() {
        return """
        Options:
        Redraw Chess Board:    "redraw"
        Leave Game:            "leave"
        Make a Move:           "move" <INITIAL POSITION> <DESIRED POSITION>
        Resign:                "resign"
        Highlight Legal Moves: "highlight" <PIECE POSITION>
        Help:                  "help"
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