package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Bid;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.timer.LiveAuctionFinishedException;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.timer.LiveAuctionTimer;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.timer.LiveAuctionTimerSettings;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.notification.AuctionNotifier;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.Result;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.error.ResultError;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.generic.PayloadResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Component
public class LiveAuctionsManagerImpl implements LiveAuctionsManager, LiveAuctionObserver {
    private final AuctionNotifier auctionNotifier;
    private final LiveAuctionsService liveAuctionsService;
    private final Map<Long, LiveAuction> liveAuctions = new ConcurrentHashMap<>();

    @Override
    public void start(Auction auction) {
        startImpl(auction);
        notifyActiveAuctionsRefreshed();
    }

    @Override
    public void startAll(Collection<Auction> auctions) {
        auctions.forEach(this::startImpl);
        notifyActiveAuctionsRefreshed();
    }

    private void startImpl(Auction auction) {
        var liveAuction = new LiveAuction(auction, this);
        var auctionTimerSettings = new LiveAuctionTimerSettings(60, 5);
        var auctionTimer = new LiveAuctionTimer(liveAuction, auctionTimerSettings);
        liveAuction.setLiveAuctionTimer(auctionTimer);
        this.liveAuctions.put(auction.getId(), liveAuction);
        auctionTimer.start();
    }

    @Override
    public PayloadResult<Bid> addBid(LiveAuction liveAuction, User user) throws LiveAuctionFinishedException {
        if (liveAuction == null || user == null) {
            throw new IllegalArgumentException(String.format("Parameters %s and %s must not be null.", LiveAuction.class, User.class));
        }
        var result = liveAuction.addBid(user);
        if (result.isError()) {
            return result;
        }
        Bid bid = result.getPayload();
        auctionNotifier.bidPlaced(bid);
        return PayloadResult.success(bid);
    }

    @Override
    public PayloadResult<LiveAuction> join(Long auctionId, User user) {
        if (auctionId == null || user == null) {
            throw new IllegalArgumentException(String.format("Parameters %s and %s must not be null.", "auctionId", User.class));
        }
        Optional<LiveAuction> liveAuctionOpt = findLiveAuction(auctionId);
        if (liveAuctionOpt.isEmpty()) {
            return PayloadResult.failure(ResultError.notFound(LiveAuction.class, "ID", auctionId.toString()));
        }
        LiveAuction liveAuction = liveAuctionOpt.get();
        liveAuction.join(user);
        user.getJoinedAuctions().add(liveAuction);
        return PayloadResult.success(liveAuction);
    }

    @Override
    public Result unjoin(Long auctionId, Long userId) {
        if (auctionId == null || userId == null) {
            throw new IllegalArgumentException(String.format("Parameters %s and %s must not be null.", "auctionId", "userId"));
        }
        Optional<LiveAuction> liveAuctionOpt = findLiveAuction(auctionId);
        if (liveAuctionOpt.isEmpty()) {
            return Result.failure(ResultError.notFound(LiveAuction.class, "ID", auctionId.toString()));
        }
        LiveAuction liveAuction = liveAuctionOpt.get();
        var userOpt = liveAuction.findJoinedUser(userId);
        if (userOpt.isEmpty()) {
            return Result.failure(ResultError.badRequest("The user is not joined to the auction."));
        }
        User user = userOpt.get();
        liveAuction.unjoin(user);
        user.getJoinedAuctions().remove(liveAuction);
        return Result.success();
    }

    @Override
    public Optional<LiveAuction> findLiveAuction(long auctionId) {
        return Optional.ofNullable(this.liveAuctions.get(auctionId));
    }

    @Override
    public List<LiveAuction> getLiveAuctions() {
        return this.liveAuctions.values().stream().toList();
    }

    @Override
    public void auctionFinished(LiveAuction liveAuction) {
        Long auctionId = liveAuction.getAuction().getId();
        if (!this.liveAuctions.containsKey(auctionId)) {
            throw new NoSuchElementException(String.format("Active auction with the ID: %s is not found.", auctionId));
        }
        this.liveAuctions.remove(auctionId);
        auctionNotifier.auctionFinished(liveAuction);
        notifyActiveAuctionsRefreshed();
        liveAuctionsService.saveFinished(liveAuction);
    }

    private void notifyActiveAuctionsRefreshed() {
        auctionNotifier.activeAuctionsRefreshed(getLiveAuctions());
    }
}
