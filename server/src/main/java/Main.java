import chess.*;
import dataaccess.DataAccessException;
import server.Server;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        String optionSelect;
        if (args.length > 0){
            optionSelect = args[0];
        } else {
            optionSelect = "memory";
        }

        try{
            Server newServer = new Server(optionSelect);
            newServer.run(8080);
        } catch (Throwable ex){
            System.out.printf("Unable to start server: %s%n", ex.getMessage());

        }

    }
}