package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.player;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlayerService {
    Page<Player> getAllAvailable(Pageable pageable);
}
