package com.nordeus.jobfair.auctionservice.auctionservice.socket.config;

import com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.all.AllAuctionsSocketHandler;
import com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.specific.AuctionSocketHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@AllArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final AuctionSocketHandler auctionSocketHandler;
    private final AllAuctionsSocketHandler allAuctionsSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(auctionSocketHandler, auctionSocketHandler.PATH);
        registry.addHandler(allAuctionsSocketHandler, allAuctionsSocketHandler.PATH);
    }
}
