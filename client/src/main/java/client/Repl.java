package client;

public class Repl{
    private final PreLoginClient preLoggedClient;

    public Repl(String serverUrl){
        preLoggedClient = new PreLoginClient(serverUrl);
    }
}