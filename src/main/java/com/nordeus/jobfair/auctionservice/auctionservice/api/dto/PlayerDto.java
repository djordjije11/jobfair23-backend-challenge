package com.nordeus.jobfair.auctionservice.auctionservice.api.dto;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Player;

public record PlayerDto(
        Long id,
        String firstName,
        String lastName
) {
    public static PlayerDto map(Player player) {
        if (player == null) {
            return null;
        }
        return new PlayerDto(player.getId(), player.getFirstName(), player.getLastName());
    }
}
