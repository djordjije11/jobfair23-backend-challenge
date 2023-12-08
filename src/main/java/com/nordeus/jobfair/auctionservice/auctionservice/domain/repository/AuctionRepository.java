package com.nordeus.jobfair.auctionservice.auctionservice.domain.repository;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
