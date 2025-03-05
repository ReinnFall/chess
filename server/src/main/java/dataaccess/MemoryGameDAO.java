package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO{
    private int nextID = 1;
    final private Collection<GameData> gameDataCollection = new HashSet<>();

    public void clearGameData() {
        gameDataCollection.clear();
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
}