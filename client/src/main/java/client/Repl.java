package client;

import java.util.Scanner;

public class Repl{
    private ClientState client;
    private String serverUrl;


    public Repl(String serverUrl){
        client = new PreLoginClient(serverUrl);
        this.serverUrl = serverUrl;
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Chess. Sign in to start.\n");
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
                    case "register":
                        client = new PostLoginClient(serverUrl);
                        //System.out.println(result);
                        System.out.print("Successfully Logged In");
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