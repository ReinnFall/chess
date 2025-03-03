package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO{

    @Override
    public UserData getUser(String username){
        return null; /// Search for the userName in collection
    }
}