package com.zlagoda.employee;

import com.zlagoda.utils.RandomUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class EmployeeIdGenerator {
    private static final int ID_LENGTH = 10;
    private static final String ID_PREFIX = "E";
    private static final String DIVIDER = "-";

    public String generate() {
        Instant now = Instant.now();
        String epochSeconds = String.valueOf(now.getEpochSecond()).substring(6);
        int randomInt = RandomUtils.randomInt(100, 1000);
        String id = ID_PREFIX + DIVIDER + epochSeconds + DIVIDER + randomInt;
        return id.substring(0, ID_LENGTH);
    }

}