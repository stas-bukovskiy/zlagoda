package com.zlagoda.product;

import com.zlagoda.helpers.DTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements DTO {

    @Nullable
    private Long id;

    @NotNull(message = "product name can not be null")
    @Size(min = 1, max = 50, message = "product name length must be between 1 and 50 characters")
    private String name;

    @NotNull(message = "product characteristics can not be null")
    @Size(min = 1, max = 50, message = "product name length must be between 1 and 50 characters")
    private String characteristics;

    @NotNull(message = "product category can not be null")
    private Long categoryId;

}
