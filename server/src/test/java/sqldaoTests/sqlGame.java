package sqldaoTests;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class sqlGame {
    private MySqlGameDAO gameDAO;

    @BeforeEach
    public void setup() throws DataAccessException{
        gameDAO = new MySqlGameDAO();
        gameDAO.clearGameData();
    }
    @Test
    public void createGamePositive() throws DataAccessException{
        String gameName = "Losers";
        int gameBYID = gameDAO.createGame(gameName);

        try (var connection = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, gameName, whiteUsername, blackUsername, `game` " +
                    "FROM GameData WHERE gameID = ?";
            try(PreparedStatement stmt = connection.prepareStatement(statement)){
                stmt.setInt(1,gameBYID);
                try(var result = stmt.executeQuery()){
                    assertTrue(result.next());
                    assertEquals(gameBYID, result.getInt("gameID"));
                    assertEquals(gameName, result.getString("gameName"));
                }
            }
        }catch(Exception ex){
            throw new DataAccessException(500,"Test failed");
//            ex.printStackTrace();
//            fail("Test failed: " + ex.getMessage());
        }
    }
    @Test
    public void createGameNegative() throws DataAccessException {
        String gameName = "Losers";
        gameDAO.createGame(gameName);

        //Can't have 2 games named the same thing
        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () ->{
            gameDAO.createGame(gameName);
        });
    }
}