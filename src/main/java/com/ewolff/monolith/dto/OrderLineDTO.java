package com.ewolff.monolith.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineDTO {

    private Long id;
    private Long itemId;
    private String itemName;
    private Integer count;
    private Double price;


}
