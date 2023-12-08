package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.*;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.primitives.Tokens;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.repository.*;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.jpa.JpaLoader;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuctionsManager;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.timer.LiveAuctionFinishedException;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.Result;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.error.ResultError;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.generic.PayloadResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final LiveAuctionsManager liveAuctionsManager;

    @Override
    public Collection<LiveAuction> getAllActive() {
        return liveAuctionsManager.getLiveAuctions();
    }

    @Override
    public Optional<Auction> getAuction(Long auctionId) {
        var auction = auctionRepository.findById(auctionId);
        auction.ifPresent(a -> JpaLoader.load(a.getPlayer(), a.getBids()));
        return auction;
    }

    @Override
    public Optional<LiveAuction> getActiveAuction(Long auctionId) {
        return liveAuctionsManager.findLiveAuction(auctionId);
    }

    @Override
    public PayloadResult<LiveAuction> join(Long auctionId, User user) {
        if (user.getId() == null || !userRepository.existsById(user.getId())) {
            userRepository.save(user);
        }
        var dbUserOpt = userRepository.findById(user.getId());
        if (dbUserOpt.isEmpty()) {
            return PayloadResult.failure(ResultError.notFound(User.class, "ID", user.getId().toString()));
        }
        User dbUser = dbUserOpt.get();
        return liveAuctionsManager.join(auctionId, dbUser);
    }

    @Override
    public Result unjoin(Long auctionId, Long userId) {
        return liveAuctionsManager.unjoin(auctionId, userId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public PayloadResult<Bid> bid(Long auctionId, Long userId) throws LiveAuctionFinishedException {
        if (!userRepository.existsById(userId)) {
            return PayloadResult.failure(ResultError.notFound(User.class, "ID", userId.toString()));
        }

        var liveAuctionOpt = liveAuctionsManager.findLiveAuction(auctionId);
        if (liveAuctionOpt.isEmpty()) {
            return PayloadResult.failure(ResultError.notFound(Auction.class, "ID", auctionId.toString()));
        }
        LiveAuction liveAuction = liveAuctionOpt.get();

        var userOpt = liveAuction.findJoinedUser(userId);
        if (userOpt.isEmpty()) {
            return PayloadResult.failure(ResultError.badRequest(
                    String.format("The user with ID: %s is not joined to the auction with ID: %s", userId, auctionId)
            ));
        }
        User user = userOpt.get();

        Tokens userTokens = userRepository.findUserTokensById(userId).getTokens();
        user.setTokens(userTokens);

        return liveAuctionsManager.addBid(liveAuction, user);
    }

    @Override
    public void addAll(Collection<Auction> auctions) {
        auctions.forEach(a -> {
            var player = playerRepository.getReferenceById(a.getPlayer().getId());
            player.setAvailable(false);
            playerRepository.save(player);
        });
        auctionRepository.saveAll(auctions);
        liveAuctionsManager.startAll(auctions);
    }
}
