package com.zlagoda.category;

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
public class CategoryDto implements DTO {

    @Nullable
    private Long id;

    @NotNull
    @Size(min = 1, max = 50, message = "category name length must be between 1 and 50 characters")
    private String name;

}
