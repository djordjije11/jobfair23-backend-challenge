package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.primitives.Tokens;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class Auction {
    @Setter
    @Getter
    @Id
    @GeneratedValue
    private Long id;
    @Setter
    @Getter
    @NonNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Player player;
    @Setter
    @Getter
    @NonNull
    private Tokens askPrice;
    @Setter
    @Getter
    private boolean finished;

    @Getter
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "auction", cascade = CascadeType.MERGE)
    private final List<Bid> bids = new ArrayList<>();

    public Optional<User> getBidder() {
        if (this.bids.isEmpty()) {
            return Optional.empty();
        }
        return bids.stream().max(Comparator.comparing(Bid::getTokens)).map(Bid::getBidder);
    }

    public Optional<Tokens> getAuctionValue() {
        if (this.bids.isEmpty()) {
            return Optional.empty();
        }
        return bids.stream().max(Comparator.comparing(Bid::getTokens)).map(Bid::getTokens);
    }
}
