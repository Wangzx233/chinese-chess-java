package com.example.sgsserver.model;

import com.example.sgsserver.websocket.WebsocketServer;

import javax.websocket.Session;

public class User {
    private Session conn;

    private String RoomID;
    private String uid;

    public Boolean Role;
    public Boolean Ready;

    public User(String uid,Session conn,String RoomID){
        this.conn=conn;
        this.uid=uid;
        this.RoomID=RoomID;
    }

    public User(){
        this.Ready=false;
    }

    public String getRoomID() {
        return RoomID;
    }

    public Session getConn() {
        return conn;
    }

    public String getUid() {
        return uid;
    }

    public void setRoomID(String roomID) {
        RoomID = roomID;
    }

    public void setConn(Session conn) {
        this.conn = conn;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
