package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO{
    final private Collection<AuthData> authData = new HashSet<>();

    public void createAuth(AuthData data){

    }
}