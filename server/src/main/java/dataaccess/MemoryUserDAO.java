package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
    final private Collection<UserData> userDataCollection = new HashSet<>();

    public UserData getUser(String username){
        for ( UserData data : userDataCollection){
            if (Objects.equals(data.username(), username)){ // change with equals
                return data;
            }
        }
        return null;
    }
    public void createUser(UserData data){
        UserData newUserData = new UserData(data.username(), data.password(), data.email());
        userDataCollection.add(newUserData);
    }
    public void clearUserData(){
        userDataCollection.clear();
    }
}