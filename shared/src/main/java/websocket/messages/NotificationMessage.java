package websocket.messages;

import com.google.gson.Gson;

public class NotificationMessage extends ServerMessage{
    private String message;

    public NotificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }
    public String getNotification(){
        return message;
    }
    public String toString() {
        return new Gson().toJson(this);
    }
}