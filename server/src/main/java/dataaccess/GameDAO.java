package dataaccess;

import model.GameData;

public interface GameDAO{
    public void clearGameData();
    public int createGame(String gameName) throws DataAccessException;
}