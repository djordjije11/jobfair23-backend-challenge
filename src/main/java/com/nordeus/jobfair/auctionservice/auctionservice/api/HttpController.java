package com.nordeus.jobfair.auctionservice.auctionservice.api;

import com.nordeus.jobfair.auctionservice.auctionservice.api.dto.*;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.AuctionService;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.LiveAuction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.auction.live.timer.LiveAuctionFinishedException;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.shared.result.error.ResultError;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/auctions")
public class HttpController {
    private final AuctionService auctionService;

    @PostMapping("{auctionId}/bid")
    public ResponseEntity<Object> bid(@PathVariable Long auctionId, @RequestBody UserIdDto userIdDto)
            throws LiveAuctionFinishedException {
        var result = auctionService.bid(auctionId, userIdDto.userId());
        return result.isError() ? handleError(result.getError()) : ResponseEntity.ok(BidDto.map(result.getPayload()));
    }

    @PostMapping("{auctionId}/join")
    public ResponseEntity<Object> join(@PathVariable Long auctionId, @RequestBody UserDto userDto) {
        var result = auctionService.join(auctionId, userDto.createUser());
        return result.isError() ? handleError(result.getError()) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/active")
    public ResponseEntity<Collection<LiveAuctionDto>> getAllActive() {
        Collection<LiveAuction> activeAuctions = auctionService.getAllActive();
        return ResponseEntity.ok(activeAuctions.stream().map(LiveAuctionDto::map).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionDto> get(@PathVariable Long id) {
        var auctionOpt = auctionService.getAuction(id);
        return auctionOpt.map(auction -> ResponseEntity.ok(AuctionDto.map(auction)))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<Set<UserDto>> getJoinedUsers(@PathVariable Long id) {
        var liveAuctionOpt = auctionService.getActiveAuction(id);
        return liveAuctionOpt.map(la -> ResponseEntity.ok(
                        la.getJoinedUsers().stream().map(UserDto::map).collect(Collectors.toSet())
                ))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private ResponseEntity<Object> handleError(ResultError error) {
        switch (error.code()) {
            case NotFound -> {
                return ResponseEntity.notFound().build();
            }
            case BadRequest -> {
                return ResponseEntity.badRequest().body(new ResponseMessage(true, error.message()));
            }
            default -> {
                return ResponseEntity.internalServerError().build();
            }
        }
    }
}
