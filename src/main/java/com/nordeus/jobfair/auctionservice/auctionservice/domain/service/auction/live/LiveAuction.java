package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Bid;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.primitives.Tokens;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.timer.LiveAuctionFinishedException;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.timer.LiveAuctionTimer;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.error.ResultError;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.generic.PayloadResult;
import lombok.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional(propagation = Propagation.NEVER)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class LiveAuction {
    @Setter
    @Getter
    @EqualsAndHashCode.Include
    @NonNull
    private Auction auction;
    @ToString.Exclude
    @NonNull
    private final LiveAuctionObserver liveAuctionObserver;
    @ToString.Exclude
    @Getter
    @Setter
    private LiveAuctionTimer liveAuctionTimer;

    @Getter
    @ToString.Exclude
    private final Set<User> joinedUsers = Collections.synchronizedSet(new HashSet<>());
    @Getter
    @NonNull
    private Tokens currentBidValue = new Tokens(0);
    @Getter
    private User bidder;

    @ToString.Exclude
    @JsonIgnore
    private transient boolean bidLocked;

    public Tokens getCurrentPrice() {
        return currentBidValue.amount() == 0 ? auction.getAskPrice() : currentBidValue.plus(1);
    }

    public Optional<User> findJoinedUser(long userId) {
        return this.joinedUsers
                .stream().filter(u -> userId == u.getId())
                .findFirst();
    }

    public boolean isUserJoined(long userId) {
        return this.joinedUsers.stream().anyMatch(u -> userId == u.getId());
    }

    public void join(User user) {
        this.joinedUsers.add(user);
    }

    public void unjoin(User user) {
        this.joinedUsers.remove(user);
    }

    public PayloadResult<Bid> addBid(User user) throws LiveAuctionFinishedException {
        if (this.auction.isFinished()) {
            return PayloadResult.failure(ResultError.badRequest("The auction is finished."));
        }
        if (user == null || !isUserJoined(user.getId())) {
            return PayloadResult.failure(ResultError.badRequest("The user is not joined to the auction."));
        }

        if (this.bidLocked) {
            return PayloadResult.failure(ResultError.badRequest("Another user has already placed the bid for the current auction value. Refresh."));
        }
        this.bidLocked = true;
        var result = addBidImpl(user);
        this.bidLocked = false;
        if (result.isError()) {
            return result;
        }
        Bid bid = result.getPayload();

        this.liveAuctionTimer.bidPlaced();
        return PayloadResult.success(bid);
    }

    private PayloadResult<Bid> addBidImpl(User user) {
        Tokens currentPrice = getCurrentPrice();
        if (user.getTokens().compareTo(currentPrice) < 0) {
            return PayloadResult.failure(ResultError.badRequest("The user does not have enough tokens."));
        }

        var bid = new Bid(this.auction, user, currentPrice);
        this.auction.getBids().add(bid);

        this.currentBidValue = currentPrice;
        this.bidder = user;

        return PayloadResult.success(bid);
    }

    public void finish() {
        if (this.auction.isFinished()) {
            return;
        }
        this.auction.setFinished(true);
        this.liveAuctionObserver.auctionFinished(this);
    }
}
