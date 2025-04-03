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

    @AfterEach
    public void takeDown() throws DataAccessException{
        authDAO.clearAuthData();
        userDAO.clearUserData();
    }

    @Test
    public void createAuthPositive() throws DataAccessException{
        UserData person = new UserData("James", "Stock","js@mail" );
        userDAO.createUser(person);

        AuthData data = new AuthData("token","James");
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
    @Test
    public void createAuthNegative(){
        AuthData badData = new AuthData("token",null);
        //throws an error if user isn't in UserData
        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () ->{
            authDAO.createAuth(badData);
        });
    }
    @Test
    public void getAuthPositive() throws DataAccessException {
        UserData person = new UserData("James", "Stock","js@mail" );
        userDAO.createUser(person);

        AuthData data = new AuthData("token","James");
        authDAO.createAuth(data);

        AuthData actual = authDAO.getAuth("token");
        assertEquals(data.authToken(),actual.authToken());
        assertEquals(data.username(),actual.username());
    }

    @Test
    public void getAuthNegative() throws DataAccessException {
        UserData person = new UserData("James", "Stock", "js@mail");
        userDAO.createUser(person);

        AuthData data = new AuthData("token", "James");
        authDAO.createAuth(data);

        String badToken = "151";
        AuthData result =  authDAO.getAuth(badToken);
        assertNull(result); // null means there was no AuthData by the token given
    }
    @Test
    public void clearAuthPositive() throws DataAccessException{
        UserData person = new UserData("James", "Stock","js@mail" );
        userDAO.createUser(person);

        AuthData data = new AuthData("token","James");
        authDAO.createAuth(data);

        authDAO.clearAuthData();

        AuthData result = authDAO.getAuth("James");
        assertNull(result); // null means there was no AuthData by the token given
    }
    @Test
    public void deleteAuthPositive() throws DataAccessException, SQLException {
        UserData person = new UserData("James", "Stock","js@mail" );
        userDAO.createUser(person);

        AuthData data = new AuthData("token","James");
        authDAO.createAuth(data);

        authDAO.deleteAuth(data);

        AuthData result = authDAO.getAuth("James");
        assertNull(result); // null means there was no AuthData by the token given
    }
    @Test
    public void deleteAuthNegative() throws DataAccessException, SQLException {
        AuthData data = new AuthData("token","James");
        authDAO.deleteAuth(data); //No issues if it tries to delete a token that isn't there
    }
}