package com.zlagoda.check;

import com.zlagoda.helpers.DTO;
import com.zlagoda.sale.SaleDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckDto implements DTO {

    @Nullable
    private String checkNumber;

    @Nullable
    private String employeeId;

    @Nullable
    private String cardNumber;

    @NotNull(message = "sales can not be null")
    @Size(min = 1, message = "check must contain at least 1 sale")
    private List<SaleDto> sales;

    @Nullable
    private Date printDate;

    @Nullable
    private BigDecimal totalSum;

    @Nullable
    private BigDecimal vat;

}
