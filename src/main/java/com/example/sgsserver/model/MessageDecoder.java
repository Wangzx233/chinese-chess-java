package com.example.sgsserver.model;

import javax.websocket.DecodeException;
import javax.websocket.EndpointConfig;

import com.alibaba.fastjson.JSON;
import com.example.sgsserver.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageDecoder implements javax.websocket.Decoder.Text<Message> {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(EndpointConfig arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public Message decode(String message) throws DecodeException {
        System.out.println(message);
        return JSON.parseObject(message, Message.class);
    }


    @Override
    public boolean willDecode(String arg0) {
        return true;
    }

}

