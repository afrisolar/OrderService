package com.afrisol.OrderService.service;

import com.afrisol.OrderService.dto.PaymentResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderConsumer {
    private final OrderService orderService;

    public OrderConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "payment", groupId = "payment-consumer-group2", containerFactory = "kafkaListenerContainerFactory")
    public void listen(PaymentResponseDto paymentResponse) {
        log.info("Received message from topic 'payment': {}", paymentResponse);

        orderService.processOrder(paymentResponse)
                .then()
                .doOnError(error -> log.error("Error processing order: {}", error.getMessage()))
                .subscribe();
    }

}
