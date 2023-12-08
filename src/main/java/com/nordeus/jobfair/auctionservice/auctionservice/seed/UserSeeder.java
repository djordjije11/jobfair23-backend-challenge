package com.nordeus.jobfair.auctionservice.auctionservice.seed;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.primitives.Tokens;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Component
public class UserSeeder {
    private final UserRepository userRepository;

    @Transactional
    public List<User> seed() {
        var random = new Random();
        var users = new LinkedList<User>();
        for (int i = 0; i < 20; i++) {
            var user = new User(
                    Seeder.getRandomName(random),
                    Seeder.getRandomName(random),
                    new Tokens(random.nextInt(1000))
            );
            users.add(user);
        }
        return userRepository.saveAll(users);
    }
}
