package com.zlagoda.employee;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

@Component
public class EmployeeIdGenerator {
    private static final int ID_LENGTH = 10;
    private static final String ID_PREFIX = "EMP";
    private static final Random RANDOM = new SecureRandom();

    public String generate() {
        Instant now = Instant.now();
        long epochSeconds = now.getEpochSecond();
        int randomInt = RANDOM.nextInt(1000000);
        String idSuffix = String.format("%06d", randomInt);
        String id = ID_PREFIX + epochSeconds + idSuffix;
        return id.substring(0, ID_LENGTH);
    }

}