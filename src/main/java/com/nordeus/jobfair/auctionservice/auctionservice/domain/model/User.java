package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.primitives.Tokens;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuction;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private Tokens tokens;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "owner")
    private final Set<Player> players = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Transient
    private final Set<LiveAuction> joinedAuctions = new HashSet<>();

    public User(Long id) {
        this.id = id;
    }

    public User(String firstName, String lastName, Tokens tokens) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.tokens = tokens;
    }
}
