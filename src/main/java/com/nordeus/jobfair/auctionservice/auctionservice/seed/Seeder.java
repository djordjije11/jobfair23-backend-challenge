package com.nordeus.jobfair.auctionservice.auctionservice.seed;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Random;

@AllArgsConstructor
@Component
public class Seeder implements CommandLineRunner {
    private final PlayerSeeder playerSeeder;
    private final UserSeeder userSeeder;

    @Override
    public void run(String... args) throws Exception {
        userSeeder.seed();
        playerSeeder.seed();
    }

    public static String getRandomName() {
        return getRandomNameImpl(new Random());
    }

    public static String getRandomName(Random random) {
        return getRandomNameImpl(random);
    }

    private static String getRandomNameImpl(Random random) {
        String letters = "abcdefghijklmnjoprstufchdczd";
        int lettersCount = 5;
        String name = "";
        var stringBuilder = new StringBuilder();
        for (int i = 0; i < lettersCount; i++) {
            char letter = letters.charAt(random.nextInt(letters.length()));
            String letterAsString = String.valueOf(letter);
            if (i == 0) {
                letterAsString = letterAsString.toUpperCase();
            }
            stringBuilder.append(letterAsString);
        }
        return stringBuilder.toString();
    }
}
