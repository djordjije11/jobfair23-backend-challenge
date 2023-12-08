package com.nordeus.jobfair.auctionservice.auctionservice.api.controllers;

import com.nordeus.jobfair.auctionservice.auctionservice.api.dto.UserDto;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        User dbUser = userService.register(userDto.createUser());
        return ResponseEntity.ok(UserDto.map(dbUser));
    }
}
