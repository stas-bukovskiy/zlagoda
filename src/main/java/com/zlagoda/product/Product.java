package com.zlagoda.product;

import com.zlagoda.helpers.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Entity {

    private Long id;
    private String name;
    private String characteristics;
    private Date expirationDate;
    private Long categoryId;

}
