package com.nordeus.jobfair.auctionservice.auctionservice.socket.handler.auction.specific;

import com.nordeus.jobfair.auctionservice.auctionservice.api.dto.PlayerDto;
import com.nordeus.jobfair.auctionservice.auctionservice.api.dto.UserDto;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuction;
import lombok.Getter;

@Getter
public class AuctionFinishedNotification {
    private final String message = "Auction is finished.";
    private final Long auctionId;
    private final PlayerDto player;
    private final Integer tokensPrice;
    private final UserDto winner;

    public AuctionFinishedNotification(Long auctionId, PlayerDto player, Integer tokensPrice, UserDto winner) {
        this.auctionId = auctionId;
        this.player = player;
        this.tokensPrice = tokensPrice;
        this.winner = winner;
    }

    public static AuctionFinishedNotification map(LiveAuction liveAuction) {
        Auction auction = liveAuction.getAuction();
        return new AuctionFinishedNotification(
                auction.getId(),
                PlayerDto.map(auction.getPlayer()),
                liveAuction.getCurrentBidValue().amount(),
                UserDto.map(liveAuction.getBidder())
        );
    }
}
