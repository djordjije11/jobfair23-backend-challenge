package com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.all;

import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;

public interface AllActiveAuctionsSocketSessionsManager {
    void addSession(WebSocketSession session);
    Collection<WebSocketSession> getSessions();
}
