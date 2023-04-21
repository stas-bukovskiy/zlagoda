package com.zlagoda.check;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "zlagoda.check")
public class CheckProperties {

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private BigDecimal vat = BigDecimal.valueOf(0.2);

}
