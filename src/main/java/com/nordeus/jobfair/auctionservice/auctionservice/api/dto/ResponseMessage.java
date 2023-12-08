package com.nordeus.jobfair.auctionservice.auctionservice.api.dto;

public record ResponseMessage (
        boolean isError,
        String message
) {
}
