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
        GameData newGameData = new GameData(nextID++,"",
                "",gameName,newGame);
        gameDataCollection.add(newGameData);
        // I need the original ID before it was incremented
        return nextID - 1;
    }
    public Collection<GameData> listGames(){
        return gameDataCollection;
    }
    public GameData getGame(int id) throws DataAccessException {
        for ( GameData data : gameDataCollection) {
            if (Objects.equals(data.gameID(), id)) {
                return data;
            }
        }
        return null;
    }
    public void updateGame(GameData gameData, String playerColor, String username) throws DataAccessException {
        gameDataCollection.remove(gameData);
        GameData updatedGameData;

        if (Objects.equals(playerColor, "WHITE")) {
            updatedGameData = new GameData(gameData.gameID(), username, "", gameData.gameName(), gameData.game());
            gameDataCollection.add(updatedGameData);
        }
        else{
            updatedGameData = new GameData(gameData.gameID(), "", username, gameData.gameName(), gameData.game());
            gameDataCollection.add(updatedGameData);
        }
    }



}