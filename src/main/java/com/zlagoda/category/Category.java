package com.zlagoda.category;

import com.zlagoda.helpers.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Entity {

    private Long id;
    private String name;

}
