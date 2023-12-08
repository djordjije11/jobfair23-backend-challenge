package com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.specific;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Component
public class AuctionsSocketSessionsManagerImpl implements AuctionsSocketSessionsManager {
    private final Map<Long, AuctionSocketSessionSet> auctionSocketSessionSets = new ConcurrentHashMap<>();

    @Override
    public synchronized void addSession(WebSocketSession session, Long auctionId) {
        AuctionSocketSessionSet auctionSocketSessionSet = this.auctionSocketSessionSets.get(auctionId);
        if (auctionSocketSessionSet == null) {
            auctionSocketSessionSet = new AuctionSocketSessionSet(auctionId);
            this.auctionSocketSessionSets.put(auctionId, auctionSocketSessionSet);
        }
        auctionSocketSessionSet.addSession(session);
    }

    @Override
    public Optional<AuctionSocketSessionSet> findAuctionSessionSet(Long auctionId) {
        return Optional.ofNullable(this.auctionSocketSessionSets.get(auctionId));
    }

    @Override
    public void removeAuctionSessionSet(Long auctionId) {
        this.auctionSocketSessionSets.remove(auctionId);
    }
}
