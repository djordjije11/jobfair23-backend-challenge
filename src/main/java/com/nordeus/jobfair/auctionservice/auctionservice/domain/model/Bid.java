package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.primitives.Tokens;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Entity
public class Bid {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "auction_id")
    private Auction auction;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User bidder;
    private Tokens tokens;

    //    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp created;

    public Bid(Auction auction, User bidder, Tokens tokens) {
        this.auction = auction;
        this.bidder = bidder;
        this.tokens = tokens;
        created = new Timestamp(System.currentTimeMillis());
    }
}
