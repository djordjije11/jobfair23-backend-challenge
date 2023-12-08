package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.generator;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.AuctionService;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.primitives.Tokens;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.player.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class AuctionGenerator {
    private final static int AUCTION_GENERATION_RATE = 60000;
    private final static int AUCTION_GENERATION_COUNT = 10;
    private final static int AUCTION_DEFAULT_ASK_PRICE = 1;

    private final AuctionService auctionService;
    private final PlayerService playerService;


    @Scheduled(fixedRate = AUCTION_GENERATION_RATE)
    public void generateAuctions() {
        log.info("Starting auctions generation.");
        var players = playerService.getAllAvailable(PageRequest.of(0, AUCTION_GENERATION_COUNT)).toList();
        if (players.isEmpty()) {
            return;
        }
        var auctions = players.stream().map(player -> new Auction(player, new Tokens(AUCTION_DEFAULT_ASK_PRICE))).toList();
        auctionService.addAll(auctions);
        log.info("Finished auctions generation.");
    }
}
