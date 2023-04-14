package com.zlagoda.utils;

import java.security.SecureRandom;
import java.util.Random;

public final class RandomUtils {

    private static final Random RANDOM = new SecureRandom();

    public static int randomInt(int from, int to) {
        return RANDOM.nextInt(to - from + 1) + from;
    }

    public static String randomUPC() {
        StringBuilder sb = new StringBuilder();

        sb.append("0");
        for (int i = 1; i <= 11; i++) {
            sb.append(RANDOM.nextInt(10));
        }

        int sum = 0;
        for (int i = 0; i < sb.length(); i++) {
            int digit = Character.getNumericValue(sb.charAt(i));
            if (i % 2 == 0) {
                sum += digit * 3;
            } else {
                sum += digit;
            }
        }
        int checkDigit = (10 - (sum % 10)) % 10;

        sb.append(checkDigit);

        return sb.toString();
    }


}
