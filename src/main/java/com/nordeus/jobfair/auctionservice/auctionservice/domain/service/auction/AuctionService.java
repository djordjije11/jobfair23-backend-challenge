package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Bid;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.timer.LiveAuctionFinishedException;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.Result;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.generic.PayloadResult;

import java.util.Collection;
import java.util.Optional;

public interface AuctionService {
    Collection<LiveAuction> getAllActive();

    Optional<Auction> getAuction(Long auctionId);

    Optional<LiveAuction> getActiveAuction(Long auctionId);

    PayloadResult<LiveAuction> join(Long auctionId, User user);
    Result unjoin(Long auctionId, Long userId);

    PayloadResult<Bid> bid(Long auctionId, Long userId) throws LiveAuctionFinishedException;

    void addAll(Collection<Auction> auctions);
}
