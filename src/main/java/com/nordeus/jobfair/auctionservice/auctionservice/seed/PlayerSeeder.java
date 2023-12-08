package com.nordeus.jobfair.auctionservice.auctionservice.seed;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Player;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Component
public class PlayerSeeder {
    private final PlayerRepository playerRepository;

    @Transactional
    public List<Player> seed() {
        var players = new LinkedList<Player>();
        for (int i = 0; i < 200; i++) {
            var player = new Player(Seeder.getRandomName(), Seeder.getRandomName(), true);
            players.add(player);
        }
        return playerRepository.saveAll(players);
    }
}
