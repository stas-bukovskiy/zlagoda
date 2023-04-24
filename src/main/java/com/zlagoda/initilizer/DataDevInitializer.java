package com.zlagoda.initilizer;

import com.zlagoda.employee.Employee;
import com.zlagoda.employee.EmployeeRepository;
import com.zlagoda.employee.EmployeeRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

@Profile("dev")
@Component
@Slf4j
@RequiredArgsConstructor
public class DataDevInitializer {

    private final EmployeeRepository repository;

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    @EventListener(value = ApplicationReadyEvent.class)
    public void initEmployees() {
        if (!repository.findAll().isEmpty())
            return;
        log.debug("[DATA_INITIALIZER] start employee data initialization...");
        Stream.of(
                        new Employee(
                                null,
                                "manager",
                                "password",
                                "surname",
                                "name",
                                "patronymic",
                                EmployeeRole.MANAGER,
                                BigDecimal.valueOf(758),
                                parseDate("2001-02-14"),
                                parseDate("2014-02-14"),
                                "+380981714821",
                                "city",
                                "street",
                                "83390"
                        ),
                        new Employee(
                                null,
                                "cashier",
                                "password",
                                "surname 1",
                                "name 1",
                                "patronymic 1",
                                EmployeeRole.CASHIER,
                                BigDecimal.valueOf(758),
                                parseDate("1999-07-21"),
                                parseDate("2015-89-12"),
                                "+380981714821",
                                "city 1",
                                "street 1",
                                "6793"
                        )
                )
                .peek(repository::save)
                .forEach(employee -> log.debug("[DATA_INITIALIZER] saved: {}", employee));
        log.debug("[DATA_INITIALIZER] done initialization...");
    }


}