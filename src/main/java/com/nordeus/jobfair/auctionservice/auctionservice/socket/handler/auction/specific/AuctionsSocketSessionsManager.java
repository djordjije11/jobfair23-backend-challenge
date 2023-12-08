package com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.specific;

import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

public interface AuctionsSocketSessionsManager {
    void addSession(WebSocketSession session, Long auctionId);
    Optional<AuctionSocketSessionSet> findAuctionSessionSet(Long auctionId);
    void removeAuctionSessionSet(Long auctionId);
}
