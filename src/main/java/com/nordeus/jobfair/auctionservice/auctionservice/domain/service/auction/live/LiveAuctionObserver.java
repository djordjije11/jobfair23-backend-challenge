package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live;

public interface LiveAuctionObserver {
    void auctionFinished(LiveAuction liveAuction);
}
