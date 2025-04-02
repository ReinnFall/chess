package sqldaoTests;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.MySqlUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class sqlUser {
    private MySqlUserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new MySqlUserDAO();
        userDAO.clearUserData();
    }

    @Test
    public void createUserNegative() throws DataAccessException{
        UserData badData = new UserData("James",null, "js@mail");

        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () ->{
            userDAO.createUser(badData);
        });
    }
    @Test
    public void createUserPositive() throws DataAccessException{
        UserData goodData = new UserData("James","Stock", "js@mail");
        userDAO.createUser(goodData);

        try (var connection = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM UserData WHERE username = ?";
            try(PreparedStatement stmt = connection.prepareStatement(statement)){
                stmt.setString(1,"James");
                try(var result = stmt.executeQuery()){
                    assertTrue(result.next());
                    assertEquals("James", result.getString("username"));
                    assertEquals("js@mail", result.getString("email"));
                    assertNotEquals("Stock", result.getString("password"));
                }
            }
        }catch(Exception ex){
            throw new DataAccessException(500,"Test failed");
        }
    }

    @Test
    public void getUserPositive() throws DataAccessException{

    }
}




