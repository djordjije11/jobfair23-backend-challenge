package com.nordeus.jobfair.auctionservice.auctionservice.socket.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class SocketMessageServiceImpl implements SocketMessageService {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void send(WebSocketSession session, Object obj) throws IOException {
        session.sendMessage(new TextMessage(gson.toJson(obj)));
    }

    @Override
    public <T> T mapMessage(WebSocketMessage<?> message, Class<T> tClass) {
        return gson.fromJson(message.getPayload().toString(), tClass);
    }
}
