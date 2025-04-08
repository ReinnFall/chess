package client;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ServerFacade{
    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
    }
    public AuthData login(UserData data) throws ResponseException{
        AuthData authData = makeRequest("POST","/session", data, AuthData.class,null);
        authToken = authData.authToken();
        return authData;
    }
    public AuthData register(UserData data) throws ResponseException {
        AuthData authData = makeRequest("POST","/user", data, AuthData.class,null);
        authToken = authData.authToken();
        return authData;
    }
    public void logout() throws ResponseException {
        makeRequest("DELETE","/session",null,null,authToken);
        authToken = null; // Clear authToken
    }
    public int createGame(GameData gameData) throws ResponseException{
        CreateGameData onlyIDData = makeRequest("POST","/game",gameData, CreateGameData.class,authToken);
        int gameID = onlyIDData.gameID();
        return gameID;
    }
    public List<GameData> listGames() throws ResponseException{
        ListGameData games = makeRequest("GET","/game",null, ListGameData.class,authToken);
        return games.games();
    }
    public void joinGame(int gameID,String teamColor) throws ResponseException {
        JoinGameData data = new JoinGameData( teamColor, gameID);
        makeRequest("PUT","/game",data,null, authToken);
    }
    public void clear() throws ResponseException{
        makeRequest("DELETE","/db", null,null,null);
    }
    //Testing purposes
    public String getAuth(){
        return authToken;
    }
    public void setAuth(String auth){
        authToken = auth;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String token) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);


            if (token != null){
                http.setRequestProperty("Authorization",token);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);

            return readBody(http, responseClass);

        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}