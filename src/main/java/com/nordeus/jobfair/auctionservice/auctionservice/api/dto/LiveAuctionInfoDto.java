package com.nordeus.jobfair.auctionservice.auctionservice.api.dto;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuction;

public record LiveAuctionInfoDto(
        Long id,
        PlayerDto player,
        Integer askPrice,
        boolean finished
) {
    public static LiveAuctionInfoDto map(LiveAuction liveAuction) {
        Auction auction = liveAuction.getAuction();
        return new LiveAuctionInfoDto(
                auction.getId(),
                PlayerDto.map(auction.getPlayer()),
                auction.getAskPrice().amount(),
                auction.isFinished()
        );
    }
}
