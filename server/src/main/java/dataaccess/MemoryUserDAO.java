package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryUserDAO implements UserDAO{
    final private Collection<UserData> userData = new HashSet<>();

    @Override
    public UserData getUser(String username){
        for ( UserData data : userData){
            if (data.username() == username){ // change with equals
                return data;
            }
        }
        return null;
    }
}