package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO{
    final private Collection<AuthData> authDataCollection = new HashSet<>();

    public void createAuth(AuthData data){
        authDataCollection.add(data);
    }
    public AuthData getAuth(String token){
        for ( AuthData currentToken : authDataCollection){
            if (Objects.equals(currentToken.authToken(), token)){ // change with equals
                return currentToken;
            }
        }
        return null;
    }
    public void deleteAuth(AuthData data){
        authDataCollection.remove(data);
    }
    public void clearAuthData(){
        authDataCollection.clear();
    }
}