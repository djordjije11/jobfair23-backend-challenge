package com.nordeus.jobfair.auctionservice.auctionservice.api.dto;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuction;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record LiveAuctionDto(
        Long id,
        PlayerDto player,
        Integer askPrice,
        boolean finished,
        UserDto bidder,
        Integer currentValue,
        List<BidDto> bids,
        Set<UserDto> joinedUsers
) {
    public static LiveAuctionDto map(LiveAuction liveAuction) {
        Auction auction = liveAuction.getAuction();
        return new LiveAuctionDto(
                auction.getId(),
                PlayerDto.map(auction.getPlayer()),
                auction.getAskPrice().amount(),
                auction.isFinished(),
                UserDto.map(liveAuction.getBidder()),
                liveAuction.getCurrentBidValue().amount(),
                auction.getBids().stream().map(BidDto::map).toList(),
                liveAuction.getJoinedUsers().stream().map(UserDto::map).collect(Collectors.toSet())
        );
    }
}
