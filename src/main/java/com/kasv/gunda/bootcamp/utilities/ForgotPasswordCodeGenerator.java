package com.kasv.gunda.bootcamp.utilities;

public class ForgotPasswordCodeGenerator {
    public static String generateCode() {
        String randomChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int randomIndex = (int) (randomChars.length() * Math.random());
            code.append(randomChars.charAt(randomIndex));
        }
        return code.toString();
    }
}
