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


        return null;
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()){
            var statement = "INSERT INTO UserData (name, type, json) VALUES (?, ?, ?)";
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
    public void clearUserData() {

    }
}