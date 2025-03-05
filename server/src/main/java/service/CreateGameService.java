package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;

public class CreateGameService{
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public int createGameRequest(GameData data, String token){



        return 0;
    }
}