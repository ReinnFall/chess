package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO{
    public void clearGameData();
    public int createGame(String gameName) throws DataAccessException;
    public Collection<GameData> listGames();
}