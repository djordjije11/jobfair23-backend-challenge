package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
public class Player {
    @GeneratedValue
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private boolean available;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User owner;

    public Player(String firstName, String lastName, boolean available) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.available = available;
    }
}
