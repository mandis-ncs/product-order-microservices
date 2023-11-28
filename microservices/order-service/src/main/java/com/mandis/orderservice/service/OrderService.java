package com.mandis.orderservice.service;

import com.mandis.orderservice.dto.OrderLineItemsDto;
import com.mandis.orderservice.dto.OrderRequest;
import com.mandis.orderservice.model.Order;
import com.mandis.orderservice.model.OrderLineItems;
import com.mandis.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        //for each item of order, map request to object, set in list, save as the list of items of the order
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        //call inventory service and place order if pro product is in stock
        Boolean result = webClient.get()
                        .uri("http://localhost:8082/api/inventory")
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block();

        if (result) {
            orderRepository.save(order);
        }
        else {
            throw new IllegalArgumentException("Product out of stock.");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
