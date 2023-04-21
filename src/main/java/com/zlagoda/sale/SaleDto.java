package com.zlagoda.sale;

import com.zlagoda.helpers.DTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleDto implements DTO {

    @NotBlank(message = "upc can not be null")
    private String storeProductUpc;

    @Nullable
    private String checkNumber;

    @NotNull(message = "product number can not be null")
    @Min(value = 1, message = "product number must  be larger than 1")
    private int productNumber;

    @Nullable
    private BigDecimal sellingPrice;

}
