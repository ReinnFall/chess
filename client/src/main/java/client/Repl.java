package client;

import java.util.Scanner;

public class Repl{
    private ClientState client;
    private ServerFacade server;


    public Repl(String serverUrl){
        server = new ServerFacade(serverUrl);
        client = new PreLoginClient(server);
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
}