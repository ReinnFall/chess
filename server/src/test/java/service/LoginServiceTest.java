package service;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoginService;

class LoginServiceTest{
    @BeforeEach
    UserData test = new UserData("James","Stock",null);
    String expectedAuthUsername = "James";
    @Test
    AuthData actual = LoginRequest(test);

}