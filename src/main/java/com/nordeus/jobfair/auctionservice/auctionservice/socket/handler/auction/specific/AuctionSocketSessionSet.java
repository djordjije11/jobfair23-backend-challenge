package com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.specific;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuctionSocketSessionSet {
    @Getter
    private final Long auctionId;
    private final Map<String, WebSocketSession> socketSessions = new ConcurrentHashMap<>();

    public AuctionSocketSessionSet(Long auctionId) {
        this.auctionId = auctionId;
    }

    public void addSession(WebSocketSession session) {
        this.socketSessions.put(session.getId(), session);
    }

    public Collection<WebSocketSession> getSessions() {
        return this.socketSessions.values().stream().toList();
    }
}

