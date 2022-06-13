package com.example.sgsserver.model;


import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;

import com.alibaba.fastjson.JSON;

public class MessageEncoder implements javax.websocket.Encoder.Text<Message> {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(EndpointConfig arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public String encode(Message message) throws EncodeException {
        return JSON.toJSONString(message);
    }

}

