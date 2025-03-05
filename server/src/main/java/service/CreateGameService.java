package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

public class CreateGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public int createGameRequest(GameData data, String token) throws DataAccessException {
        AuthData authFromMemory = authDAO.getAuth(token);
        if(authFromMemory == null){
            throw new DataAccessException(401,"Error: unauthorized");
        }
        else{
            int gameID = gameDAO.createGame(data.gameName());
            return gameID;
        }
    }
}