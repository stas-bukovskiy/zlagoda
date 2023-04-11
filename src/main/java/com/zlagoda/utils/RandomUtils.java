package com.zlagoda.utils;

import java.security.SecureRandom;
import java.util.Random;

public final class RandomUtils {

    private static final Random RANDOM = new SecureRandom();

    public static int randomInt(int from, int to) {
        return RANDOM.nextInt(to - from + 1) + from;
    }


}
