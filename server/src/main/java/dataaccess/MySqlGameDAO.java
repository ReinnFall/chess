package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> tempCollection = new HashSet<>();
        try (var connection = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, gameName, whiteUsername, blackUsername, `game`" +
                    "FROM GameData";
            try(PreparedStatement stmt = connection.prepareStatement(statement)){
                try(var resultStatement = stmt.executeQuery()){
                    while(resultStatement.next()){
                        int gameID = resultStatement.getInt("gameID");
                        String gameName = resultStatement.getString("gameName");
                        String whiteUsername = resultStatement.getString("whiteUsername");
                        String blackUsername = resultStatement.getString("blackUsername");
                        String gameJson = resultStatement.getString("game");

                        ChessGame chessGameFromDB = deserialzer(gameJson);
                        GameData oneGame = new GameData(gameID,whiteUsername,blackUsername,gameName,chessGameFromDB);
                        tempCollection.add(oneGame);
                    }
                }
            }
        } catch (SQLException | DataAccessException ex){
            throw new DataAccessException(500,"Failed to list games");
        }
        return tempCollection;
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
    public void updateGame(GameData gameData, String playerColor, String username) throws DataAccessException, SQLException {
        GameData currentGame = getGame(gameData.gameID());
        if (currentGame == null){
            throw new DataAccessException(404, "Game not found");
        }

        String statement;
        if (Objects.equals(playerColor, "WHITE")){
            if(currentGame.whiteUsername() != null){
                throw new DataAccessException(403, "Error: already taken");
            }
            statement = "UPDATE GameData SET whiteUsername = ? WHERE gameID = ?";
        } else if (Objects.equals(playerColor,"BLACK")){
            if(currentGame.blackUsername() != null){
                throw new DataAccessException(403, "Error: already taken");
            }
            statement = "UPDATE GameData SET blackUsername = ? WHERE gameID = ?";
        } else{
            throw new DataAccessException(400,"Error: bad request");
        }
        try (var connection = DatabaseManager.getConnection()){
            try(PreparedStatement stmt = connection.prepareStatement(statement)){
                stmt.setString(1,username);
                stmt.setInt(2,gameData.gameID());
                stmt.executeUpdate();
            }
        }catch(Exception ex){
            throw new DataAccessException(500,"Failed to update game");
        }
    }
}