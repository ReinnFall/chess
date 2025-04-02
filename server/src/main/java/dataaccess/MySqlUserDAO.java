package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS UserData (
                username varchar(255) PRIMARY KEY,
                password varchar(255) NOT NULL,
                email varchar(255) NOT NULL
            )
            """
    };

    public MySqlUserDAO() throws DataAccessException{
        ConfigureDatabase.configureDatabase(createStatements);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email" + "FROM UserData" +
                    "WHERE username = ?";
            try(PreparedStatement stmt = connection.prepareStatement(statement)){
                stmt.setString(1,username);
                try(var resultStatement = stmt.executeQuery()){
                    if (resultStatement.next()){
                        String usernameFromDB = resultStatement.getString("username");
                        String passwordFromDB = resultStatement.getString("password");
                        String emailFromDB = resultStatement.getString("email");
                        return new UserData(usernameFromDB,passwordFromDB,emailFromDB);
                    }
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new DataAccessException(500, "Could not find user");
        }
        return null;
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        if (data.username() == null || data.password() == null  || data.email() == null){
            throw new DataAccessException(401,"Error: Invalid user input");
        }
        try (var connection = DatabaseManager.getConnection()){
            var statement = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?)";
            String hashedPassword = BCrypt.hashpw(data.password(), BCrypt.gensalt());

            try(PreparedStatement stmt = connection.prepareStatement(statement)){
                stmt.setString(1,data.username());
                stmt.setString(2,hashedPassword);
                stmt.setString(3,data.email());
                stmt.executeUpdate();
            }
        } catch (DataAccessException | SQLException ex){
            throw new DataAccessException(500, "Could not create user");
        }
    }

    @Override
    public void clearUserData() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()){
            var statement = "DELETE FROM UserData";
            try(PreparedStatement stmt = connection.prepareStatement(statement)){
                stmt.executeUpdate();
            }
        } catch (DataAccessException | SQLException ex){
            throw new DataAccessException(500, "Error clearing user data");
        }
    }
}