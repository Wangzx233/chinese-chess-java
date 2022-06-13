package com.example.sgsserver.model;

public class Message implements java.io.Serializable {

    public int Type;   //消息类型 -1:报错 0:聊天信息 1:加入房间 2:准备就绪 3:操作游戏
    public String From;
    public String To;
    public String Content;

    private String RoomID;

    public Message(int type, String from, String to, String content, String roomID) {
        Type = type;
        From = from;
        To = to;
        Content = content;
        RoomID = roomID;
    }

    public Message() {
    }

    public void setRoomID(String roomID) {
        RoomID = roomID;
    }

    public String getRoomID() {
        return RoomID;
    }

    public Message(int type, String from, String to, String content){
        this.Type=type;
        this.From=from;
        this.To=to;
        this.Content=content;
    }
}
