package com.afrisol.OrderService.service;

import com.afrisol.OrderService.dto.OrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderProducer {

    private static final String TOPIC = "order-placed";

    private final KafkaTemplate<String, OrderResponse> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, OrderResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(OrderResponse orderResponse) {
        log.info("Publishing message to Kafka topic - order-placed: {}", orderResponse);
        kafkaTemplate.send(TOPIC, orderResponse);
    }
}
