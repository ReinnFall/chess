package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class ListGameService{
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public ListGameService(AuthDAO authDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public Collection<GameData>listGameRequest(String token) throws DataAccessException {
        AuthData authFromMemory = authDAO.getAuth(token);
        if(authFromMemory == null){
            throw new DataAccessException(401,"Error: unauthorized");
        }
        else{
            Collection<GameData> games = gameDAO.listGames();
        }
        return null;
    }
}