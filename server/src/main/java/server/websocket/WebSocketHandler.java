package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
   // WebSocketHandler(UserDAO, user)
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class); // gets type
        switch (command.getCommandType()) {
            case CONNECT -> connect();
            case MAKE_MOVE -> makeMOVE(new Gson().fromJson(message, MakeMoveCommand.class)); //deserialize a second time to get the move
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }
    private void connect(){

    }
    private void makeMOVE(MakeMoveCommand command){
        // gameDAO.
    }
    private void leave(){

    }
    private void resign(){

    }
}