package com.example.sgsserver.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Rooms{
    public static ConcurrentMap<String, User[]> RoomMap = new ConcurrentHashMap<>();

    //将新的连接加入房间 key 为房间号
    public static void put(String roomID, User newUser) {
        if (RoomMap.get(roomID)==null){
            User[] newArr = new User[1];
            newArr[0]=newUser;
            RoomMap.put(roomID, newArr);
            return;
        }
        //房间已满
        if (RoomMap.get(roomID).length>2)
        {
            return;
        }
        User[] users = RoomMap.get(roomID);

        User[] newArr = new User[users.length+1];

        for (int i = 0; i < users.length; i++) {
            newArr[i]=users[i];
        }
        newArr[users.length]=newUser;

        RoomMap.put(roomID, newArr);
    }
    public static void Join(String roomID, User newUser) {
        User[] users = RoomMap.get(roomID);
        List<User> list = new ArrayList<>();
        if (users!=null)
        {
            list = Arrays.asList(users);
            //房间已满
            if (list.size()>=2)
            {
                return;
            }
        }

        list.add(newUser);

        User[] newArr = list.toArray(new User[list.size()]);
        RoomMap.put(roomID, newArr);
    }

    public static User[] get(String roomID) {

        return RoomMap.get(roomID);
    }

//    public static String[] showRooms()
//    {
//        String[] f ={};
//        List<String> list1 = Arrays.asList(f);
//        System.out.println(RoomMap.keySet());
//        list1.addAll(RoomMap.keySet());
//
//        return list1.toArray(new String[]);
//    }
//
    public static void removeRoom(String roomID) {
        RoomMap.remove(roomID);
    }

    public static void removeUser(String roomID,String Uid) {
        //先清空房间
        User[] users = RoomMap.get(roomID);
        System.out.println(users.length);
        RoomMap.remove(roomID);
        //遍历之前房间里的用户
        for (int i = 0; i < users.length; i++) {

            //如果不是要删除的用户就加入房间
            if (!Objects.equals(users[i].getUid(), Uid))
            {
                Rooms.put(roomID,users[i]);
            }
        }

    }

    public static Collection<User[]> getValues() {
        return RoomMap.values();
    }


}
