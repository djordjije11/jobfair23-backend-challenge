package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.notification;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Bid;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
//@Service
public class AuctionNotifierLogger implements AuctionNotifier {

    @Override
    public void auctionFinished(LiveAuction auction) {
        log.info("Auction finished: {}", auction);
    }

    @Override
    public void bidPlaced(Bid bid) {
        log.info("Bid placed: {}", bid);
    }

    @Override
    public void activeAuctionsRefreshed(Collection<LiveAuction> activeAuctions) {
        log.info("Active auctions are refreshed: {}", activeAuctions);
    }
}
