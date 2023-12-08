package com.nordeus.jobfair.auctionservice.auctionservice.socket.service;

import org.springframework.web.socket.WebSocketSession;

public interface SocketAuthService {
    Long getUserId(WebSocketSession session);
}
