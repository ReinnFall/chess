package sqldaoTests;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class sqlAuth {
    private MySqlAuthDAO authDAO;
    private MySqlUserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        authDAO = new MySqlAuthDAO();
        authDAO.clearAuthData();

        userDAO = new MySqlUserDAO();
        userDAO.clearUserData();
    }

    @Test
    public void createAuthPositive() throws DataAccessException{
        UserData person = new UserData("James", "Stock","js@mail" );
        userDAO.createUser(person);

        AuthData data = new AuthData("James","token");
        authDAO.createAuth(data);

        try (var connection = DatabaseManager.getConnection()){
            var statement = "SELECT authToken, username FROM AuthData WHERE authToken = ?";
            try(PreparedStatement stmt = connection.prepareStatement(statement)){
                stmt.setString(1,"token");
                try(var result = stmt.executeQuery()){
                    assertTrue(result.next());
                    assertEquals("token", result.getString("authToken"));
                    assertEquals("James", result.getString("username"));
                }
            }
        }catch(Exception ex){
            throw new DataAccessException(500,"Test failed");
        }
    }
}