package com.nordeus.jobfair.auctionservice.auctionservice.api.dto;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.primitives.Tokens;

import java.util.List;

public record AuctionDto(
        Long id,
        PlayerDto player,
        Integer askPrice,
        boolean finished,
        List<BidDto> bids,
        UserDto bidder,
        Integer currentValue
) {
    public static AuctionDto map(Auction auction) {
        return new AuctionDto(
                auction.getId(),
                PlayerDto.map(auction.getPlayer()),
                auction.getAskPrice().amount(),
                auction.isFinished(),
                auction.getBids().stream().map(BidDto::map).toList(),
                auction.getBidder().map(UserDto::map).orElse(null),
                auction.getAuctionValue().map(Tokens::amount).orElse(null)
        );
    }
}
