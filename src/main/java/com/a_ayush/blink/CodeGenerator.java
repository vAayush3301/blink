package com.a_ayush.blink;

import java.security.SecureRandom;

public class CodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generateCode() {
        StringBuilder code = new StringBuilder(6);

        for (int i = 0; i < 2; i++) {
            code.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        }

        for (int i = 0; i < 4; i++) {
            code.append(RANDOM.nextInt(10));
        }

        return code.toString();
    }
}