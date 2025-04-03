package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public String serializer(ChessGame gameJava){
        return new Gson().toJson(gameJava);
    }

    @Override
    public void clearGameData() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()){
            var statement = "DELETE FROM GameData";
            try(PreparedStatement stmt = connection.prepareStatement(statement)){
                stmt.executeUpdate();
            }
        } catch (DataAccessException | SQLException ex){
            throw new DataAccessException(500, "Error clearing game data");
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()){
            var statement = "INSERT INTO GameData (gameName, whiteUsername, blackUsername, game) VALUES (?, ?, ?, ?)";
            ChessGame newGame = new ChessGame();
            String serializedGame = serializer(newGame);

            try(PreparedStatement stmt = connection.prepareStatement(statement)){
                stmt.setString(1, gameName);
                stmt.setString(2,null);
                stmt.setString(3,null);
                stmt.setString(4,serializedGame);
                stmt.executeUpdate();

                try(var indexes = stmt.getGeneratedKeys()){
                    if(indexes.next()){
                        return indexes.getInt(1);
                    } else{
                        throw new DataAccessException(500, "Error getting id");
                    }
                }
            }
        } catch (DataAccessException | SQLException ex){
            throw new DataAccessException(500, "Could not create game");
        }
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