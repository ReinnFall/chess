package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String visitorName;
    public Session session;
    public int gameID;
    public String position;

    public Connection(String visitorName, Session session, int gameID, String position) {
        this.visitorName = visitorName;
        this.session = session;
        this.gameID = gameID;
        this.position = position;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
    public int getID(){
        return gameID;
    }
    public String getPosition(){
        return position;
    }
}