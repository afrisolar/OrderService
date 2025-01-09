package com.afrisol.OrderService.service;


import com.afrisol.OrderService.dto.OrderNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NotificationProducerTest {
    @Mock
    private KafkaTemplate<String, OrderNotification> kafkaTemplate;

    @InjectMocks
    private NotificationProducer notificationProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMessage_ShouldSendMessageToKafkaTopic() {
        OrderNotification orderNotification = OrderNotification.builder()
                .orderNumber("ORDER123")
                .status("COMPLETED")
                .message("Order completed successfully")
                .build();

        notificationProducer.sendMessage(orderNotification);
        verify(kafkaTemplate, times(1)).send("order-complete", orderNotification);
    }

}
