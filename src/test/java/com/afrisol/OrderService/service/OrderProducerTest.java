package com.afrisol.OrderService.service;

import com.afrisol.OrderService.dto.OrderResponse;
import com.afrisol.OrderService.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
public class OrderProducerTest {

    @Mock
    private KafkaTemplate<String, OrderResponse> kafkaTemplate;

    @InjectMocks
    private OrderProducer orderProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMessage_ShouldSendMessageToKafkaTopic() {
        OrderResponse mockOrderResponse = new OrderResponse();
        mockOrderResponse.setOrderNumber("ORDER123");
        mockOrderResponse.setStatus(OrderStatus.PENDING);
        orderProducer.sendMessage(mockOrderResponse);
        verify(kafkaTemplate, times(1)).send("order-placed", mockOrderResponse);
    }
}
