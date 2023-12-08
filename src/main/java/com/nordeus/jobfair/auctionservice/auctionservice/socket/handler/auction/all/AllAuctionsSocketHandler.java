package com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.all;

import com.nordeus.jobfair.auctionservice.auctionservice.api.dto.LiveAuctionDto;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.AuctionService;
import com.nordeus.jobfair.auctionservice.auctionservice.socket.service.SocketMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@AllArgsConstructor
@Component
public class AllAuctionsSocketHandler extends TextWebSocketHandler {
    private final AllActiveAuctionsSocketSessionsManager allActiveAuctionsSocketSessionsManager;
    private final AuctionService auctionService;
    private final SocketMessageService socketMessageService;

    public final String PATH = "/auction/all";

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        allActiveAuctionsSocketSessionsManager.addSession(session);
        var activeAuctionsDto = auctionService.getAllActive()
                .stream().map(LiveAuctionDto::map).toList();
        socketMessageService.send(session, activeAuctionsDto);
    }
}
