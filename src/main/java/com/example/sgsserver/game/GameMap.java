package com.example.sgsserver.game;

import com.example.sgsserver.model.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GameMap {
    public static ConcurrentMap<String, Game> GameMap = new ConcurrentHashMap<>();

    public static void NewGame(String roomID)
    {
        Game game = new Game();
        GameMap.put(roomID,game);
    }

    public static Game GetGame(String roomID)
    {
        return GameMap.get(roomID);
    }

    public static void RemoveGame(String roomID)
    {
        GameMap.remove(roomID);
    }
    public static void UpdateGame(String roomID,Game game)
    {
        GameMap.put(roomID,game);
    }
}
