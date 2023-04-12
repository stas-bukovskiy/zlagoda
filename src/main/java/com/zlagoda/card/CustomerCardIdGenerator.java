package com.zlagoda.card;

import com.zlagoda.utils.RandomUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CustomerCardIdGenerator {

    private static final int ID_LENGTH = 13;
    private static final String DIVIDER = "-";

    public String generate() {
        Instant now = Instant.now();
        String epochSeconds = String.valueOf(now.getEpochSecond()).substring(3);
        int randomInt = RandomUtils.randomInt(10000, 100000);
        String id = epochSeconds + DIVIDER + randomInt;
        return id.substring(0, ID_LENGTH);
    }

}
