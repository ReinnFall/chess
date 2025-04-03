package dataaccess;

import model.GameData;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Collection;

public interface GameDAO{
    public void clearGameData() throws DataAccessException;
    public int createGame(String gameName) throws DataAccessException;
    public Collection<GameData> listGames();
    public GameData getGame(int id) throws DataAccessException, SQLException;
    public void updateGame(GameData gameData, String playerColor, String username) throws DataAccessException;
}