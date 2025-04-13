package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    private static final ConnectionManager connections = new ConnectionManager();
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public WebSocketHandler(UserDAO userDAO,AuthDAO authDAO,GameDAO gameDAO){
       this.userDAO = userDAO;
       this.authDAO = authDAO;
       this.gameDAO = gameDAO;
   }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, SQLException, InvalidMoveException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class); // gets type
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, command);
            case MAKE_MOVE -> makeMove(session, new Gson().fromJson(message, MakeMoveCommand.class)); //deserialize a second time to get the move
            case LEAVE -> leave(session, command);
            case RESIGN -> resign(session,command);
        }
    }
    private void connect(Session session, UserGameCommand command) throws DataAccessException, SQLException, IOException {
        String authToken = command.getAuthToken();
        int gameID = command.getGameID();

        AuthData authFromDB = authDAO.getAuth(authToken);
        if(authFromDB == null){
            ErrorMessage error = new ErrorMessage("Error: Not Authorized");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }

        String username = authFromDB.username();
        GameData gameData = gameDAO.getGame(gameID);
        if(gameData == null){
            ErrorMessage error = new ErrorMessage("Error: Game not found");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }
        if(gameData.game() == null){
            ErrorMessage error = new ErrorMessage("Error: Game not found");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }
        String position;
        if (Objects.equals(username, gameData.whiteUsername())){
            position = "WHITE";
        } else if (Objects.equals(username, gameData.blackUsername())){
            position = "BLACK";
        } else{
            position = "observer";
        }

        connections.add(username,session,gameID,position);

        LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game());
        session.getRemote().sendString(new Gson().toJson(loadGameMessage));

        String joinOrWatchMessage = username + " entered the game as " + position;
        NotificationMessage notificationMessage = new NotificationMessage(joinOrWatchMessage);
        connections.broadcastExceptInitializer(username,notificationMessage,gameID);
    }
    private void makeMove(Session session, MakeMoveCommand command) throws DataAccessException, SQLException, InvalidMoveException, IOException {
        String authToken = command.getAuthToken();
        int gameID = command.getGameID();
        ChessMove move = command.getMove();

        AuthData authFromDB = authDAO.getAuth(authToken);
        if(authFromDB == null){
            ErrorMessage error = new ErrorMessage("Error: Not Authorized");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }

        String username = authFromDB.username();
        GameData gameData = gameDAO.getGame(gameID);
        if(gameData.game() == null){
            ErrorMessage error = new ErrorMessage("Error: Game not found");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }
        GameData currentGameData = gameDAO.getGame(gameID);
        ChessGame currentGame = currentGameData.game();
        currentGame.makeMove(move);

        GameData moveMadeGame = new GameData(gameID, currentGameData.whiteUsername(),
                currentGameData.blackUsername(), gameData.gameName(),currentGame);
        gameDAO.updateGame(moveMadeGame,null,null);

        LoadGameMessage loadGame = new LoadGameMessage(currentGame);
        connections.broadcastIncludingInitializer(username,loadGame,gameID);

        String notification = username + " moved from " + move.getStartPosition() + " to " + move.getEndPosition();
        NotificationMessage notifyMessage = new NotificationMessage(notification);
        connections.broadcastExceptInitializer(username,notifyMessage,gameID);

        //Handle Check/Checkmate

    }
    private void leave(Session session, UserGameCommand command) throws DataAccessException, SQLException, IOException {
        String authToken = command.getAuthToken();
        int gameID = command.getGameID();

        AuthData authFromDB = authDAO.getAuth(authToken);
        if(authFromDB == null){
            ErrorMessage error = new ErrorMessage("Error: Not Authorized");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }
        String username = authFromDB.username();
        GameData gameData = gameDAO.getGame(gameID);
        if(gameData.game() == null){
            ErrorMessage error = new ErrorMessage("Error: Game not found");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }
        //remove from gamedao
        Connection connection = connections.getConnection(username);
        if (connection != null){
            if(Objects.equals(connection.getPosition(), "BLACK") || Objects.equals(connection.getPosition(), "WHITE")){
                gameDAO.removePlayer(gameID, connection.position);
            }
        }
        connections.remove(username);
        //broadcast leave to others in the game
        String leaveMessage = username + " has left the game.";
        NotificationMessage notificationMessage = new NotificationMessage(leaveMessage);
        connections.broadcastExceptInitializer(username,notificationMessage,gameID);
    }
    private void resign(Session session, UserGameCommand command) throws DataAccessException, SQLException, IOException {
        String authToken = command.getAuthToken();
        int gameID = command.getGameID();

        AuthData authFromDB = authDAO.getAuth(authToken);
        if(authFromDB == null){
            ErrorMessage error = new ErrorMessage("Error: Not Authorized");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }
        String username = authFromDB.username();
        GameData gameData = gameDAO.getGame(gameID);
        if(gameData.game() == null){
            ErrorMessage error = new ErrorMessage("Error: Game not found");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }
        // change isOver on chessGame to true
        //output message
        String leaveMessage = username + " resigned.";
        NotificationMessage notificationMessage = new NotificationMessage(leaveMessage);
        connections.broadcastIncludingInitializer(username,notificationMessage,gameID);
    }
}