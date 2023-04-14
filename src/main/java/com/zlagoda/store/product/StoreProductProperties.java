package com.zlagoda.store.product;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "zlagoda.store.product")
public class StoreProductProperties {

    @Min(1)
    private int numberOfProductsToBePromotional = 1;

    @DurationUnit(ChronoUnit.DAYS)
    private Duration numberOfDaysToBePromotional = Duration.ofDays(7);

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private BigDecimal promotionalDiscountCoefficient = BigDecimal.valueOf(0.8);

}
