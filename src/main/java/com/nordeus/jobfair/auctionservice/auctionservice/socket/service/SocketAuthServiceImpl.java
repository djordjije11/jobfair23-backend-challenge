package com.nordeus.jobfair.auctionservice.auctionservice.socket.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class SocketAuthServiceImpl implements SocketAuthService {
    private static final String AUTH_HEADER = "Auth";

    @Override
    public Long getUserId(WebSocketSession session) {
        return Long.parseLong(session.getHandshakeHeaders().get(AUTH_HEADER).get(0));
    }
}
