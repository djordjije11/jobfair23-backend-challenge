package com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.specific;

import com.nordeus.jobfair.auctionservice.auctionservice.api.dto.LiveAuctionDto;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.AuctionService;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.generic.PayloadResult;
import com.nordeus.jobfair.auctionservice.auctionservice.socket.service.SocketAuthService;
import com.nordeus.jobfair.auctionservice.auctionservice.socket.service.SocketMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;

@AllArgsConstructor
@Component
public class AuctionSocketHandler extends TextWebSocketHandler {
    private final AuctionsSocketSessionsManager auctionsSocketSessionsManager;
    private final SocketAuthService socketAuthService;
    private final SocketMessageService socketMessageService;
    private final AuctionService auctionService;

    public final String PATH = "/auction/{auctionId}";

    private Long extractAuctionId(URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI must not be null.");
        }
        final String path = uri.getPath();
        String auctionIdString = path.substring(PATH.lastIndexOf("/") + 1);
        return Long.parseLong(auctionIdString);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//        var bidDto = socketMessageService.mapMessage(message, BidDto.class);
        var auctionId = extractAuctionId(session.getUri());
        var userId = socketAuthService.getUserId(session);
        var result = auctionService.bid(auctionId, userId);
        if (result.isError()) {
            socketMessageService.send(session, result.getError());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        var auctionId = extractAuctionId(session.getUri());
        var userId = socketAuthService.getUserId(session);
        PayloadResult<LiveAuction> liveAuctionResult = auctionService.join(auctionId, new User(userId));
        if (liveAuctionResult.isError()) {
            socketMessageService.send(session, liveAuctionResult.getError());
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        auctionsSocketSessionsManager.addSession(session, auctionId);
        socketMessageService.send(session, LiveAuctionDto.map(liveAuctionResult.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        var auctionId = extractAuctionId(session.getUri());
        var userId = socketAuthService.getUserId(session);
        auctionService.unjoin(auctionId, userId);
    }
}
