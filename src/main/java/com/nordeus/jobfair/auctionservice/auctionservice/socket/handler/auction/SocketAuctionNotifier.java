package com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction;

import com.nordeus.jobfair.auctionservice.auctionservice.api.dto.BidDto;
import com.nordeus.jobfair.auctionservice.auctionservice.api.dto.LiveAuctionInfoDto;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Bid;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.notification.AuctionNotifier;
import com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.all.AllActiveAuctionsSocketSessionsManager;
import com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.specific.AuctionFinishedNotification;
import com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.specific.AuctionSocketSessionSet;
import com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.specific.AuctionsSocketSessionsManager;
import com.nordeus.jobfair.auctionservice.auctionservice.socket.service.SocketMessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;

@Slf4j
@AllArgsConstructor
@Service
public class SocketAuctionNotifier implements AuctionNotifier {
    private final AuctionsSocketSessionsManager auctionsSocketSessionsManager;
    private final AllActiveAuctionsSocketSessionsManager allActiveAuctionsSocketSessionsManager;
    private final SocketMessageService socketMessageSender;

    @Override
    public void auctionFinished(LiveAuction liveAuction) {
        var auctionSocketSessionSetOpt =
                auctionsSocketSessionsManager.findAuctionSessionSet(liveAuction.getAuction().getId());
        if (auctionSocketSessionSetOpt.isEmpty()) {
            return;
        }
        AuctionSocketSessionSet auctionSocketSessionSet = auctionSocketSessionSetOpt.get();
        var notification = AuctionFinishedNotification.map(liveAuction);
        var socketSessions = auctionSocketSessionSet.getSessions();
        socketSessions.forEach(session -> {
            try {
                socketMessageSender.send(session, notification);
                session.close();
            } catch (IOException ex) {
                log.error("In communication with session with ID: {} - auctionFinished IOException: {}",
                        session.getId(), ex.getMessage());
            }
        });
        auctionsSocketSessionsManager.removeAuctionSessionSet(liveAuction.getAuction().getId());
    }

    @Override
    public void bidPlaced(Bid bid) {
        Long auctionId = bid.getAuction().getId();
        AuctionSocketSessionSet auctionSocketSessionSet =
                auctionsSocketSessionsManager.findAuctionSessionSet(auctionId)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("AuctionSocketSessionSet with auctionId: %s is not found.", auctionId)
                ));
        var bidDto = BidDto.map(bid);
        var socketSessions = auctionSocketSessionSet.getSessions();
        socketSessions.forEach(session -> {
            try {
                socketMessageSender.send(session, bidDto);
            } catch (IOException ex) {
                log.error("In communication with session with ID: {} - bidPlaced IOException: {}",
                        session.getId(), ex.getMessage());
            }
        });
    }

    @Override
    public void activeAuctionsRefreshed(Collection<LiveAuction> activeAuctions) {
        var socketSessions = allActiveAuctionsSocketSessionsManager.getSessions();
        var liveAuctionsDto = activeAuctions.stream().map(LiveAuctionInfoDto::map).toList();
        socketSessions.forEach(session -> {
            try {
                socketMessageSender.send(session, liveAuctionsDto);
            } catch (IOException ex) {
                log.error("In communication with session with ID: {} - activeAuctionsRefreshed IOException: {}",
                        session.getId(), ex.getMessage());
            }
        });
    }
}
