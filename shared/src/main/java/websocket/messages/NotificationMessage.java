package websocket.messages;

import com.google.gson.Gson;

public class NotificationMessage extends ServerMessage{
    private String notification;

    public NotificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        notification = message;
    }
    public String getNotification(){
        return notification;
    }
    public String toString() {
        return new Gson().toJson(this);
    }
}