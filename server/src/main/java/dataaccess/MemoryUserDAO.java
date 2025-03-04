package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
    final private Collection<UserData> userData = new HashSet<>();

    @Override
    public UserData getUser(String username){
        for ( UserData data : userData){
            if (Objects.equals(data.username(), username)){ // change with equals
                return data;
            }
        }
        return null;
    }
}