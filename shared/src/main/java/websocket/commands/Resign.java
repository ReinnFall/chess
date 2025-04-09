package websocket.commands;

import websocket.commands.UserGameCommand;

public class Resign extends UserGameCommand{

    public Resign(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }
}