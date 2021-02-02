package com.ewolff.monolith.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long id;

    private Long customerId;

    private List<OrderLineDTO> orderLine;

    private Double totalPrice;

    private String displayName;
}
