package com.zlagoda.check;

import com.zlagoda.utils.RandomUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CheckIdGenerator {
    private static final int ID_LENGTH = 10;
    private static final String DIVIDER = "-";

    public String generate() {
        Instant now = Instant.now();
        String epochSeconds = String.valueOf(now.getEpochSecond()).substring(5);
        int randomInt = RandomUtils.randomInt(1000, 10000);
        String id = epochSeconds + DIVIDER + randomInt;
        return id.substring(0, ID_LENGTH);
    }

}