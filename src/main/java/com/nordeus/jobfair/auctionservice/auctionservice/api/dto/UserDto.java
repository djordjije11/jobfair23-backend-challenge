package com.nordeus.jobfair.auctionservice.auctionservice.api.dto;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.primitives.Tokens;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        Integer tokens
) {
    public static UserDto map(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getTokens().amount());
    }

    public User createUser() {
        return new User(id, firstName, lastName, new Tokens(tokens));
    }
}
