package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAO{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS GameData (
                id int auto_increment PRIMARY KEY,
                gameName varchar(255) unique NOT NULL,
                whiteUsername varchar(255),
                blackUsername varchar(255),
                game Text,
                FOREIGN KEY (whiteUsername) references UserData(username),
                FOREIGN KEY (blackUsername) references UserData(username)
            )
            """
    };

    public MySqlGameDAO() throws DataAccessException{
        ConfigureDatabase.configureDatabase(createStatements);
    }

    @Override
    public void clearGameData() {

    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData gameData, String playerColor, String username) throws DataAccessException {

    }
}