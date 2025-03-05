package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

public class JoinGameService{
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public JoinGameService(AuthDAO authDAO,GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public void joinGameRequest(GameData data, String token) throws DataAccessException{

    }
}