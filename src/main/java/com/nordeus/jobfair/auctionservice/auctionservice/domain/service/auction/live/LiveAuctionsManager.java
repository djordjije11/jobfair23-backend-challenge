package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Bid;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.timer.LiveAuctionFinishedException;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.Result;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.generic.PayloadResult;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LiveAuctionsManager {
    void start(Auction auction);

    void startAll(Collection<Auction> auctions);

    PayloadResult<Bid> addBid(LiveAuction liveAuction, User bidder) throws LiveAuctionFinishedException;

    PayloadResult<LiveAuction> join(Long auctionId, User user);
    Result unjoin(Long auctionId, Long userId);

    Optional<LiveAuction> findLiveAuction(long auctionId);

    List<LiveAuction> getLiveAuctions();
}
