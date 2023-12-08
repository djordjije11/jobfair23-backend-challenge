package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.timer;

public class LiveAuctionFinishedException extends Exception {
    public LiveAuctionFinishedException() {
        super("Auction is not active anymore.");
    }

    public LiveAuctionFinishedException(String message) {
        super(message);
    }
}
