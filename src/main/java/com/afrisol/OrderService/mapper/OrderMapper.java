package com.afrisol.OrderService.mapper;


import com.afrisol.OrderService.dto.OrderRequest;
import com.afrisol.OrderService.dto.OrderResponse;
import com.afrisol.OrderService.model.CustomerOrder;
import org.apache.commons.lang3.RandomStringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;


@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    @Mappings({
            @Mapping(source = "orderNumber", target = "orderNumber")
    })
    OrderResponse toOrderResponse(CustomerOrder customerOrder);


    @Mappings({
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "status", expression = "java(com.afrisol.OrderService.model.OrderStatus.NEW)"),
            @Mapping(target = "orderNumber", expression = "java(generateOrderNumber())")
    })
    CustomerOrder toOrder(OrderRequest orderRequest);


    default String generateOrderNumber() {
        return "ORD-" + RandomStringUtils
                .randomAlphanumeric(4).toUpperCase();
    }
}
