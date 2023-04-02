package com.zlagoda.card;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Card {

    private String cardNumber;
    private String surname;
    private String name;
    private String patronymic;
    private String number;
    private String city;
    private String street;
    private String zipCode;
    private int percent;

}
