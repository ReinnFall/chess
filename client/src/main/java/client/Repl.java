package client;

import java.util.Scanner;

public class Repl{
    private ClientState client;


    public Repl(String serverUrl){
        client = new PreLoginClient(serverUrl);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Chess. Sign in to start.\n");
        System.out.print(client.help());

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
                        client = new PostLoginClient();
                        System.out.println(result);
                        break;
                    default:
                        System.out.print(result);
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        //System.out.println();
    }
}