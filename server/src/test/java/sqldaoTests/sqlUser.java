package sqldaoTests;

import dataaccess.DataAccessException;
import dataaccess.MySqlUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}




