package com.zlagoda.store.product;

import com.zlagoda.helpers.DTO;
import com.zlagoda.product.ProductDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreProductDto implements DTO {

    @NotNull(message = "store product upc can not be null")
    @Size(min = 1, max = 12, message = "store product upc length must be between 1 and 12 characters")
    private String upc;

    @NotNull(message = "selling price can not be null")
    @Digits(integer = 13, fraction = 4)
    @DecimalMin(value = "0.0", message = "selling price must be larger than 0.0")
    private BigDecimal sellingPrice;

    @NotNull(message = "number of products can not be null")
    @Min(value = 0, message = "number of products must be larger than 0")
    private int productsNumber;

    @Nullable
    private boolean isPromotional;

    @NotNull(message = "product can not be null")
    private ProductDto product;

    @Nullable
    private StoreProductDto promStoreProduct;
}
