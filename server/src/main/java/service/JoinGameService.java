package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.JoinGameData;

import java.sql.SQLException;

public class JoinGameService{
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public JoinGameService(AuthDAO authDAO,GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public void joinGameRequest(JoinGameData joinGameData, String token) throws DataAccessException, SQLException {
        AuthData authFromMemory = authDAO.getAuth(token);
        if(authFromMemory == null){
            throw new DataAccessException(401,"Error: unauthorized");
        }
        else{
            GameData gameData = gameDAO.getGame(joinGameData.gameID());
            if (gameData == null ){
                throw new DataAccessException(400,"Error:Bad Request");
            }
            else{
                gameDAO.updateGame(gameData,joinGameData.playerColor(),authFromMemory.username());
            }
        }
    }
}