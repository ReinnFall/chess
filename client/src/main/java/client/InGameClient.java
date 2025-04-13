package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class InGameClient implements ClientState{
    private final ServerFacade server;
    private State state = State.SIGNEDIN;
    private final ServerMessageHandler messageHandler;
    private WebSocketFacade ws;
    private final int gameID;
    private final ChessGame.TeamColor playerColor;
    private Position position;
    private ChessGame currentGame;
    TerminalChessBoard chessboardPrinter = new TerminalChessBoard();


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
        if(params.length != 1){
            return "Invalid input";
        }
        String locationOnBoard = params[0];

        if(locationOnBoard.length() != 2){
            return "Bad input";
        }
        char columnChar = locationOnBoard.charAt(0);
        char rowChar = locationOnBoard.charAt(1);

        int column = columnChar - 'a' + 1; //converts a-h to 1-8
        int row = Character.getNumericValue(rowChar); //char to int

        //Make a check for valid column and row inputs

        ChessPosition positionOFPiece = new ChessPosition(row,column);
        Collection<ChessMove> possibleMoves = currentGame.validMoves(positionOFPiece);
        if (possibleMoves == null || possibleMoves.isEmpty()){
            return "There are no possible moves";
        }

        Set<ChessPosition> endPositions = new HashSet<>();
        for (ChessMove move : possibleMoves){
            endPositions.add(move.getEndPosition());
        }
        chessboardPrinter.printChessBoard(currentGame,playerColor,positionOFPiece,endPositions);
        return "Possible moves are marked by green squares";
    }

    private String resign() throws ResponseException {
        ws.giveUp(server.getAuth(),gameID);
        return "";
    }

    private String makeMove(String... params) throws ResponseException {
        if(params.length != 2){
            return "Invalid input";
        }
        String startLocation = params[0];
        String endLocation = params[1];

        if(startLocation.length() != 2 || endLocation.length() !=2){
            return "Bad input";
        }
        char columnCharStart = startLocation.charAt(0);
        char rowCharStart = startLocation.charAt(1);
        int columnStart = columnCharStart - 'a' + 1; //converts a-h to 1-8
        int rowStart = Character.getNumericValue(rowCharStart); //char to int

        ChessPosition startPosition = new ChessPosition(rowStart,columnStart);

        char columnCharEnd = endLocation.charAt(0);
        char rowCharEnd = endLocation.charAt(1);
        int columnEnd = columnCharEnd - 'a' + 1; //converts a-h to 1-8
        int rowEnd = Character.getNumericValue(rowCharEnd); //char to int

        ChessPosition endPosition = new ChessPosition(rowEnd,columnEnd);

        ChessMove move = new ChessMove(startPosition,endPosition,null);
        ws.makeMove(server.getAuth(),gameID,move);
        return "";
    }

    private String redraw() {
        chessboardPrinter.printChessBoard(currentGame,playerColor,null,null);

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
    public void setGame(ChessGame game){
        currentGame = game;
    }

    @Override
    public String printPrompt() {
//        String status;
//        if(position == Position.PLAYER){
//            status = "[PLAYER] ";
//        } else{
//            status = "[OBSERVER] ";
//        }
//        return ("\n" + status + ">>> " );
        return "";
    }
}