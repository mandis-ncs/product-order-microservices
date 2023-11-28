package com.mandis.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}

/*
*
{
	"orderLineItemsDtoList" : [

		{
			"skuCode" : "iphone 13 pro max",
			"price" : 1200,
			"quantity" : 1
		}
	]
}
*/