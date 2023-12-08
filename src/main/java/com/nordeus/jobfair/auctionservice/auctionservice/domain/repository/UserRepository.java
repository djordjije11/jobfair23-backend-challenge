package com.nordeus.jobfair.auctionservice.auctionservice.domain.repository;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.primitives.Tokens;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    UserTokens findUserTokensById(Long userId);
    interface UserTokens {
        Tokens getTokens();
    }
}
