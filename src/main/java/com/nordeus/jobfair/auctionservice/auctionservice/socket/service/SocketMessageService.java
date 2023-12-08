package com.nordeus.jobfair.auctionservice.auctionservice.socket.service;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface SocketMessageService {
    void send(WebSocketSession session, Object obj) throws IOException;
    <T> T mapMessage(WebSocketMessage<?> message, Class<T> tClass);
}
