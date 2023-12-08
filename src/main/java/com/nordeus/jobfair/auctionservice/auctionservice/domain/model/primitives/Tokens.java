package com.nordeus.jobfair.auctionservice.auctionservice.domain.model.primitives;

import java.io.Serializable;

public record Tokens(Integer amount) implements Serializable, Comparable<Tokens> {
    @Override
    public int compareTo(Tokens o) {
        return this.amount - o.amount();
    }

    public Tokens plus(int amount) {
        return new Tokens(this.amount + amount);
    }

    public Tokens plus(Tokens tokens) {
        return new Tokens(this.amount + tokens.amount);
    }

    public Tokens minus(int amount) {
        return new Tokens(this.amount - amount);
    }

    public Tokens minus(Tokens tokens) {
        return new Tokens(this.amount - tokens.amount);
    }
}
