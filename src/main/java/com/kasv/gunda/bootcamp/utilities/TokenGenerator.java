package com.kasv.gunda.bootcamp.utilities;

import java.security.SecureRandom;


public class TokenGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final int TOKEN_LENGTH = 40; // Adjust the length as needed

    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return token.toString();
    }

    public static void main(String[] args) {
        // Example usage
        String token = generateToken();
        System.out.println("Generated Token: " + token);
    }
}
