package client;

import chess.ChessGame;
import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.MakeMoveCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class Repl implements ServerMessageHandler {
    private ClientState client;
    private ServerFacade server;
    private WebSocketFacade websocket;
    private String serverUrl;
    int gameID;
    ChessGame.TeamColor playerColor;


    public Repl(String serverUrl)  {
        server = new ServerFacade(serverUrl);
        client = new PreLoginClient(server);
       //websocket = new WebSocketFacade(serverUrl,this);
        this.serverUrl = serverUrl;
    }

    public void run() {
        System.out.println("Welcome to Chess. Sign in to start.\n");
        System.out.print(client.help());
        System.out.print(client.printPrompt());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (true) {
            try {
                String line = scanner.nextLine();
                result = client.eval(line);

                switch(result){
                    case "quit":
                        System.out.println("Exited");
                        return;
                    case "logged in":
                        client = new PostLoginClient(server);
                        //System.out.println(result);
                        System.out.print("Successfully Logged In");
                        System.out.print(client.printPrompt());
                        break;
                    case "logout":
                        client = new PreLoginClient(server);
                        System.out.print("Successfully Logged Out");
                        System.out.print(client.printPrompt());
                        break;
                    case "leave":
                        client = new PostLoginClient(server);
                        System.out.print("Successfully Logged Out");
                        System.out.print(client.printPrompt());
                        break;
                    case "join game":
                        gameID = server.getGameID();
                        playerColor = server.getPlayerColor();

                        client = new InGameClient(server,this,serverUrl,gameID,playerColor);
                        System.out.print("Entered game as a player");
                        System.out.print(client.printPrompt());
                        break;
                    case "watch game":
                        gameID = server.getGameID();
                        playerColor = null;

                        client = new InGameClient(server,this,serverUrl,gameID,playerColor);
                        System.out.print("Entered game as an observer");
                        System.out.print(client.printPrompt());
                        break;
                    default:
                        System.out.print(result);
                        System.out.print(client.printPrompt());

                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        //System.out.println();
    }
    //doesnt handle load game
    public void notify(String message) {
        //Need to add a method to serverMessage to be able to print out message
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> loadGame(new Gson().fromJson(message, LoadGameMessage.class));
            case ERROR -> displayError(new Gson().fromJson(message, ErrorMessage.class));
            case NOTIFICATION -> displayNotification(new Gson().fromJson(message, NotificationMessage.class));
        }
    }

    private void displayNotification(NotificationMessage notificationMessage) {
    }

    private void displayError(ErrorMessage errorMessage) {
    }

    private void loadGame(LoadGameMessage loadGameMessage) {
    }

}