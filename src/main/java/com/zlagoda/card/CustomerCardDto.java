package com.zlagoda.card;

import com.zlagoda.helpers.DTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCardDto implements DTO {

    private String cardNumber;

    @NotNull(message = "surname can not be null")
    @Size(max = 50, message = "surname length cannot be larger than 50 characters")
    private String surname;

    @NotNull(message = "name can not be null")
    @Size(max = 50, message = "name length cannot be larger than 50 characters")
    private String name;

    @Nullable
    @Size(max = 50, message = "patronymic length cannot be larger than 50 characters")
    private String patronymic;

    @NotNull(message = "phone number can not be null")
    @Pattern(regexp = "^\\+?\\d{10,13}$", message = "phone number can be as example: '+380123456789")
    private String phoneNumber;

    @NotNull(message = "city can not be null")
    @Size(max = 50, message = "city length cannot be larger than 50 characters")
    private String city;

    @NotNull(message = "street can not be null")
    @Size(max = 50, message = "street length cannot be larger than 50 characters")
    private String street;

    @NotNull(message = "zip code can not be null")
    @Size(max = 9, message = "zip code length cannot be larger than 9 characters")
    private String zipCode;


    @NotNull(message = "percent of discount can not be null")
    @Min(value = 0, message = "percent of discount must be larger than 0")
    @Max(value = 100, message = "percent of discount must be smaller than 100")
    private int percent;

}
