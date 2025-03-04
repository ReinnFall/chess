//package service;
//
//import dataaccess.*;
//import model.AuthData;
//import model.UserData;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import service.LoginService;
//
//import java.util.Objects;
//
//class LoginServiceTest{
//    private UserData test;
//    private String expectedAuthUsername;
//    private LoginService service;
//
//    @BeforeEach
//    public void beforeEach(){
//        test = new UserData("James","Stock",null);
//        expectedAuthUsername = "James";
//        UserDAO userDAO = new MemoryUserDAO();
//        AuthDAO authDAO = new MemoryAuthDAO();
//        service = new LoginService(userDAO,authDAO);
//
//    }
//
//    @Test
//    public void authTokenCheck() throws DataAccessException {
//        AuthData actual = service.loginRequest(test);
//
//        Assertions.assertEquals(expectedAuthUsername,actual);
//
//    }
//}