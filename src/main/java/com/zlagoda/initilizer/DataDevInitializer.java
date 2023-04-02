package com.zlagoda.initilizer;

import com.zlagoda.employee.Employee;
import com.zlagoda.employee.EmployeeRepository;
import com.zlagoda.employee.EmployeeRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

@Profile("dev")
@Component
@Slf4j
@RequiredArgsConstructor
public class DataDevInitializer {

    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getDateTime(int year, int month, int day, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, day);
        cal.set(Calendar.MINUTE, hour);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    @EventListener(value = ApplicationReadyEvent.class)
    public void initEmployees() {
        log.info("[DATA_INITIALIZER] start employee data initialization...");
        Calendar calendar = Calendar.getInstance();
        Stream.of(
                        new Employee(
                                null,
                                "manager",
                                passwordEncoder.encode("password"),
                                "surname",
                                "name",
                                "patronymic",
                                EmployeeRole.MANAGER,
                                BigDecimal.valueOf(758),
                                parseDate("2001-02-14"),
                                getDateTime(2022, 10, 23, 9),
                                "+380981714821",
                                "city",
                                "street",
                                "83390"
                        ),
                        new Employee(
                                null,
                                "cashier",
                                passwordEncoder.encode("password"),
                                "surname 1",
                                "name 1",
                                "patronymic 1",
                                EmployeeRole.CASHIER,
                                BigDecimal.valueOf(758),
                                parseDate("1999-07-21"),
                                getDateTime(2019, 10, 23, 9),
                                "+380981714821",
                                "city 1",
                                "street 1",
                                "6793"
                        )
                )
                .peek(repository::save)
                .peek(employee -> log.debug("[DATA_INITIALIZER] saved: {}", employee));
        log.info("[DATA_INITIALIZER] done initialization...");
    }


}