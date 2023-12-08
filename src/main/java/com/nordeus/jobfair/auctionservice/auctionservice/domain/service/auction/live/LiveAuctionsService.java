package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuction;

public interface LiveAuctionsService {
    void saveFinished(LiveAuction liveAuction);
}
