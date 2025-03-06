package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO{
    private int nextID = 1;
    final private Collection<GameData> gameDataCollection = new HashSet<>();

    public void clearGameData() {
        gameDataCollection.clear();
    }
    public GameData findGame(GameData gameData){
        for ( GameData data : gameDataCollection) {
            if (Objects.equals(data, gameData)){
                return data;
            }
        }
        return null;
    }
    public int createGame(String gameName) throws DataAccessException {
        for ( GameData data : gameDataCollection){
            if (Objects.equals(data.gameName(), gameName)){
                throw new DataAccessException(401, "Error: bad request");
            }
        }
        ChessGame newGame = new ChessGame();
        GameData newGameData = new GameData(nextID++,null, null,gameName,newGame);
        gameDataCollection.add(newGameData);
        // I need the original ID before it was incremented
        return nextID - 1;
    }
    public Collection<GameData> listGames(){
        Collection<GameData> tempCollection = new HashSet<>();
        //We don't need ChessGame games for our output
        //Set each game field to null but capture all the other info and push it up
        for (GameData data : gameDataCollection){
            GameData tempData = new GameData(data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), null);
            tempCollection.add(tempData);
        }
        return tempCollection;
    }
    public GameData getGame(int id) throws DataAccessException {
        for ( GameData data : gameDataCollection) {
            if (Objects.equals(data.gameID(), id)) {
                return data;
            }
        }
        return null;
    }
    // Need to prevent the same player from joining the game they are already apart of
    public void updateGame(GameData gameData, String playerColor, String username) throws DataAccessException {
        GameData updatedGameData;

        if (Objects.equals(playerColor, "WHITE")) {
            if (gameData.whiteUsername() != null) {
                throw new DataAccessException(403, "Error: already taken");
            } else {
                updatedGameData = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
                gameDataCollection.remove(gameData);
                gameDataCollection.add(updatedGameData);
            }
        }else if (Objects.equals(playerColor, "BLACK")) {
            if (gameData.blackUsername() != null) {
                throw new DataAccessException(403, "Error: already taken");
            } else {
                updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
                gameDataCollection.remove(gameData);
                gameDataCollection.add(updatedGameData);
            }
        } else{
            throw new DataAccessException(400,"Error: bad request");
        }

    }

}