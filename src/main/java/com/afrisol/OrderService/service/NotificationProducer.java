package com.afrisol.OrderService.service;

import com.afrisol.OrderService.dto.OrderNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationProducer {
    private static final String TOPIC = "order-complete";

    private final KafkaTemplate<String, OrderNotification> kafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, OrderNotification> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(OrderNotification orderNotification) {
        log.info("About to publishing message to Kafka topic order-complete: {}", orderNotification);
        kafkaTemplate.send(TOPIC, orderNotification);
    }
}
