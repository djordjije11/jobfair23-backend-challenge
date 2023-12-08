package com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.all;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AllActiveAuctionsSocketSessionsManagerImpl implements AllActiveAuctionsSocketSessionsManager {
    private final Map<String, WebSocketSession> socketSessions = new ConcurrentHashMap<>();

    @Override
    public void addSession(WebSocketSession session) {
        this.socketSessions.put(session.getId(), session);
    }

    @Override
    public Collection<WebSocketSession> getSessions() {
        return this.socketSessions.values().stream().toList();
    }
}
