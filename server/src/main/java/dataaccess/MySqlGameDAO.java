package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAO{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS GameData (
                gameID int auto_increment PRIMARY KEY,
                gameName varchar(255) unique NOT NULL,
                whiteUsername varchar(255),
                blackUsername varchar(255),
                `game` Text,
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
    public ChessGame deserialzer(String json){
        return new Gson().fromJson(json,ChessGame.class);
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

            try(PreparedStatement stmt = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
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
    public GameData getGame(int id) throws DataAccessException, SQLException {
        try (var connection = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, gameName, whiteUsername, blackUsername, `game`" +
                    "FROM GameData WHERE gameID = ?";
            try(PreparedStatement stmt = connection.prepareStatement(statement)){
                stmt.setInt(1,id);
                try(var resultStatement = stmt.executeQuery()){
                    if(resultStatement.next()){
                        String gameName = resultStatement.getString("gameName");
                        String whiteUsername = resultStatement.getString("whiteUsername");
                        String blackUsername = resultStatement.getString("blackUsername");
                        String gameJson = resultStatement.getString("game");

                        ChessGame chessGameFromDB = deserialzer(gameJson);
                        return new GameData(id,whiteUsername,blackUsername,gameName,chessGameFromDB);
                    }
                }
            }
        } catch (SQLException ex){
            throw new DataAccessException(500,"Failed to get game");
        }
        return null;
    }
    @Override
    public void updateGame(GameData gameData, String playerColor, String username) throws DataAccessException {

    }
}