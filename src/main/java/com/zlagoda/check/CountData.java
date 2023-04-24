package com.zlagoda.check;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountData<ID> {

    @Nullable
    private ID id;

    @NotNull(message = "from can not be null")
    private String from;

    @NotNull(message = "to can not be null")
    private String to;
}
