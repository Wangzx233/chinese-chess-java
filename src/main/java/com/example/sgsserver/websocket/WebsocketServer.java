package com.example.sgsserver.websocket;

import com.example.sgsserver.game.Game;
import com.example.sgsserver.game.GameMap;
import com.example.sgsserver.model.*;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


@ServerEndpoint(value = "/websocket/{userId}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
@Component
public class WebsocketServer {
    private Logger logger = Logger.getLogger(String.valueOf(WebsocketServer.class));

    /**
     * 　　* 连接建立后触发的方法
     */

    private final User user = new User();

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) throws EncodeException, IOException {


        user.setRoomID("hall");
        user.setUid(userId);
        user.setConn(session);
        //用户初始房间为"hall"，即大厅
        Rooms.put(user.getRoomID(), user);


        logger.info("onOpen" + session.getId());
        Message message = new Message(0, userId, user.getRoomID(), "成功连接");
        session.getBasicRemote().sendObject(message);
    }


    /**
     * 　　* 连接关闭后触发的方法
     */
    @OnClose
    public void onClose() {
//从map中删除
        Rooms.removeUser(user.getRoomID(), user.getUid());
        GameMap.RemoveGame(user.getRoomID());
    }


    /**
     * 　　 * 接收到客户端消息时触发的方法
     */
    @OnMessage
    public void onMessage(Message message, Session session) throws Exception {

        switch (message.Type) {
            case 0:
                //0代表发送聊天信息
                Message m = new Message(0, user.getUid(), user.getRoomID(), message.Content);
                User[] users = Rooms.get(user.getRoomID());
                //发送给所处房间中所有人
                for (User item : users) {
                    item.getConn().getBasicRemote().sendObject(m);
                }
                break;
            case 1:
                //1代表加入房间，content为房间id
                Rooms.put(message.Content, user);
                Rooms.removeUser(user.getRoomID(), user.getUid());
                user.setRoomID(message.Content);

                User[] users1 = Rooms.get(user.getRoomID());
                //发送给所处房间中所有人
                for (User value : users1) {
                    value.getConn().getBasicRemote().sendObject(new Message(0, user.getUid(), user.getRoomID(), user.getUid() + "加入了房间"));
                }
                break;
            case 2:
                //2代表切换准备状态，当房间内2人都准备时开始游戏
                //判断是否处在大厅中，如果是就无法切换准备状态
                if (Objects.equals(user.getRoomID(), "hall")) {
                    user.getConn().getBasicRemote().sendObject(new Message(-1, user.getUid(), user.getRoomID(), "处于大厅无法开始游戏"));
                    System.out.println(user.getRoomID());
                    break;
                }
                //切换当前用户准备状态
                user.Ready = !user.Ready;
                //判断当前房间内已准备的人数
                int r = 0;
                for (int i = 0; i < Rooms.get(user.getRoomID()).length; i++) {
                    if (Rooms.get(user.getRoomID())[i].Ready) {
                        r += 1;
                    }
                }
                //发送信息
                for (User u : Rooms.get(user.getRoomID())) {
                    if (user.Ready){
                        u.getConn().getBasicRemote().sendObject(new Message(0, "", "", user.getUid()+"已准备就绪"));
                    }else {
                        u.getConn().getBasicRemote().sendObject(new Message(0, "", "", user.getUid()+"已取消准备"));
                    }
                }
                //如果两人都已准备就开始游戏
                if (r == 2) {
                    //开始游戏
                    GameMap.NewGame(user.getRoomID());
                    boolean role = false;
                    //发送给所处房间中所有人他们的角色
                    for (User u : Rooms.get(user.getRoomID())) {
                        role = !role;
                        u.Role = role;
                        u.getConn().getBasicRemote().sendObject(new Message(2, "", "", Integer.toString(--r)));
                    }
                }

                break;
            case 3:
                //游戏操作
                if (user.Role==null){
                    user.getConn().getBasicRemote().sendObject(new Message(-1, "", "", "游戏未开始"));
                    break;
                }
                Game game = GameMap.GetGame(user.getRoomID());
                game.PrintGame();
                if (user.Role!=game.getNowPlayer()){
                    break;
                }
                int res = game.Operate(message.Content.substring(0,2),message.Content.substring(2,4));
                switch (res)
                {
                    case -1:
                        user.getConn().getBasicRemote().sendObject(new Message(-1, "", "", "无效操作"));
                        break;
                    case 0:
                    case 1:
                        //游戏结束公布胜利者
                        for (User u : Rooms.get(user.getRoomID())) {
                            u.getConn().getBasicRemote().sendObject(new Message(4, "", "", Integer.toString(res)));
                        }
                        break;
                    case 2:
                        //游戏操作有效，同步操作
                        for (User u : Rooms.get(user.getRoomID())) {
                            u.getConn().getBasicRemote().sendObject(new Message(3, "", "", message.Content));
                        }

                        break;
                }
        }
    }


    /**
     * 　　 * 发生错误时触发的方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.info(session.getId() + "连接发生错误" + error.getMessage());
        error.printStackTrace();
    }

    public void sendMessage(int status, String message, Object datas) throws IOException {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("message", message);
        result.put("datas", datas);
//        this.session.getBasicRemote().sendText(result.toString());
    }

}
