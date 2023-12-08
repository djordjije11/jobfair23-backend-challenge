package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.timer;

public record LiveAuctionTimerSettings(
    int durationInSeconds,
    int bidAddedRefreshTimeInSeconds
) {
}
