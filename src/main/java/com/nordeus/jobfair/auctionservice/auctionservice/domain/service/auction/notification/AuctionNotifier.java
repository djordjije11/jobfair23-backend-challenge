package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.notification;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Bid;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuction;

import java.util.Collection;

public interface AuctionNotifier {

    void auctionFinished(LiveAuction auction);

    void bidPlaced(Bid bid);

    void activeAuctionsRefreshed(Collection<LiveAuction> activeAuctions);
}
