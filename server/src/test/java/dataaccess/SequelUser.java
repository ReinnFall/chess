package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class SequelUser {
    private MySqlUserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new MySqlUserDAO();
        userDAO.clearUserData();
    }
    @AfterEach
    public void takeDown() throws DataAccessException {
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
        UserData goodData = new UserData("James","Stock", "js@mail");
        userDAO.createUser(goodData);

        UserData actual = userDAO.getUser("James");
        assertEquals("James", actual.username());
        assertEquals("js@mail", actual.email());
        assertNotEquals("Stock", actual.password());
    }
    @Test
    public void getUserNegative() throws DataAccessException{
        UserData goodData = new UserData("James","Stock", "js@mail");
        userDAO.createUser(goodData);

        String notInDB = "Jacob";

       UserData result = userDAO.getUser(notInDB);
       assertEquals(null,result); // When getUser returns null, it means that name wasn't found
    }
    @Test
    public void clearUserData() throws DataAccessException{
        UserData goodData = new UserData("James","Stock", "js@mail");
        userDAO.createUser(goodData);

        userDAO.clearUserData();

        UserData result = userDAO.getUser(goodData.username());
        assertEquals(null,result); // When getUser returns null, it means that name wasn't found
    }
}




