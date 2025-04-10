package server.websocket;

import chess.ChessGame;
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
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

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
    public void onMessage(Session session, String message) throws IOException, DataAccessException, SQLException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class); // gets type
        switch (command.getCommandType()) {
            case CONNECT -> connect(session,command);
            case MAKE_MOVE -> makeMOVE(new Gson().fromJson(message, MakeMoveCommand.class)); //deserialize a second time to get the move
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }
    private void connect(Session session, UserGameCommand command) throws DataAccessException, SQLException, IOException {
        String authToken = command.getAuthToken();
        int gameID = command.getGameID();

        AuthData authFromDB = authDAO.getAuth(authToken);
        if(authFromDB == null){
            //throw an error
            return;
        }

        String username = authFromDB.username();
        GameData gameData = gameDAO.getGame(gameID);
        if(gameData.game() == null){
            //throw and error
            return;
        }
        connections.add(username,session);

        LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game());
        session.getRemote().sendString(new Gson().toJson(loadGameMessage));

        String position;
        if (Objects.equals(username, gameData.whiteUsername())){
            position = "WHITE";
        } else if (Objects.equals(username, gameData.blackUsername())){
            position = "BLACK";
        } else{
            position = "observer";
        }

        String joinOrWatchMessage = username + " entered the game as " + position;
        NotificationMessage notificationMessage = new NotificationMessage(joinOrWatchMessage);
        connections.broadcast(username,notificationMessage);
    }
    private void makeMOVE(MakeMoveCommand command){
        // gameDAO.
    }
    private void leave(){

    }
    private void resign(){

    }
}