import chess.*;
import dataaccess.DataAccessException;
import server.Server;

public class Main {
    public static void main(String[] args) {
        try{
            Server newServer = new Server();
            newServer.run(8080);
        } catch (Throwable ex){
            System.out.printf("Unable to start server: %s%n", ex.getMessage());

        }

    }
}