package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{
    final private Collection<GameData> gameDataCollection = new HashSet<>();

    public void clearGameData() {
        gameDataCollection.clear();
    }
}