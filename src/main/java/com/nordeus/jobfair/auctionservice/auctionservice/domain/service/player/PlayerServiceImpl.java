package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.player;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Player;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    @Override
    public Page<Player> getAllAvailable(Pageable pageable) {
        return playerRepository.findAllByAvailable(true, pageable);
    }
}
