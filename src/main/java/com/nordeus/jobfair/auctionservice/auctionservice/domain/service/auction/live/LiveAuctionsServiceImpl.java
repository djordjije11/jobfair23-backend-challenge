package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Player;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.repository.AuctionRepository;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.repository.PlayerRepository;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class LiveAuctionsServiceImpl implements LiveAuctionsService {
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;

    @Override
    public void saveFinished(LiveAuction liveAuction) {
        liveAuction.finish();
        if (liveAuction.getBidder() == null) {
            saveFinishedUnsold(liveAuction.getAuction());
        } else {
            saveFinishedSold(liveAuction);
        }
    }

    private void saveFinishedUnsold(Auction auction) {
        Player player = playerRepository.findById(auction.getPlayer().getId())
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("The player with ID: %s is not found.", auction.getPlayer().getId()
                        )));
        player.setAvailable(true);
        playerRepository.save(player);
        auctionRepository.save(auction);
    }

    private void saveFinishedSold(LiveAuction liveAuction) {
        User dbBidder = userRepository.findById(liveAuction.getBidder().getId())
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("The user with ID: %s is not found.", liveAuction.getBidder().getId()
                        )));
        dbBidder.setTokens(dbBidder.getTokens().minus(liveAuction.getCurrentBidValue()));
        userRepository.save(dbBidder);
        Auction auction = liveAuction.getAuction();
        Player player = playerRepository.findById(auction.getPlayer().getId())
                .orElseThrow(NoSuchElementException::new);
        player.setOwner(dbBidder);
        playerRepository.save(player);
        auctionRepository.save(auction);
    }
}
