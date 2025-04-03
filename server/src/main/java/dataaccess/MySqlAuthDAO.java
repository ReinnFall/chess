package dataaccess;

import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDAO{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS AuthData (
                authToken varchar(255) PRIMARY KEY,
                username varchar(255) NOT NULL,
                FOREIGN KEY (username) references UserData(username)
            )
            """
    };

    public MySqlAuthDAO() throws DataAccessException{
        ConfigureDatabase.configureDatabase(createStatements);
    }

    @Override
    public void createAuth(AuthData data) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()){
            var statement = "INSERT INTO AuthData (authToken, username) VALUES (?, ?)";
            try(PreparedStatement stmt = connection.prepareStatement(statement)){
                stmt.setString(1,data.authToken());
                stmt.setString(2, data.username());
                stmt.executeUpdate();
            }
        } catch (DataAccessException | SQLException ex){
            throw new DataAccessException(500, "Could not create AuthData");
        }
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData data) throws DataAccessException {

    }

    @Override
    public void clearAuthData() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()){
            var statement = "DELETE FROM AuthData";
            try(PreparedStatement stmt = connection.prepareStatement(statement)){
                stmt.executeUpdate();
            }
        } catch (DataAccessException | SQLException ex){
            throw new DataAccessException(500, "Error clearing auth data");
        }
    }

    @Override
    public AuthData getAuthByUsername(String username) {
        return null;
    }

}