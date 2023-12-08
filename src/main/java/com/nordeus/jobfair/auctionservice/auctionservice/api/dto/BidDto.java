package com.nordeus.jobfair.auctionservice.auctionservice.api.dto;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Bid;

import java.sql.Timestamp;

public record BidDto(
        Long id,
        UserDto bidder,
        Integer tokens,
        Timestamp created
) {
    public static BidDto map(Bid bid) {
        if (bid == null) {
            return null;
        }
        return new BidDto(bid.getId(), UserDto.map(bid.getBidder()), bid.getTokens().amount(), bid.getCreated());
    }
}
