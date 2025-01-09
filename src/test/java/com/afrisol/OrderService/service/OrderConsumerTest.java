package com.afrisol.OrderService.service;

import com.afrisol.OrderService.dto.PaymentResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@Slf4j
public class OrderConsumerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderConsumer orderConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListen_WhenProcessOrderSucceeds_ShouldLogMessage() {
        PaymentResponseDto paymentResponse = new PaymentResponseDto();
        paymentResponse.setPaymentId(11L);
        paymentResponse.setCustomer("CUST456");
        paymentResponse.setTotalAmount(BigDecimal.valueOf(100));

        when(orderService.processOrder(paymentResponse)).thenReturn(Mono.empty());
        orderConsumer.listen(paymentResponse);
        verify(orderService, times(1)).processOrder(paymentResponse);
    }

    @Test
    void testListen_WhenProcessOrderFails_ShouldLogError() {
        // Arrange
        PaymentResponseDto paymentResponse = new PaymentResponseDto();
        paymentResponse.setPaymentId(11L);
        paymentResponse.setCustomer("CUST456");
        paymentResponse.setTotalAmount(BigDecimal.valueOf(100));

        when(orderService.processOrder(paymentResponse)).thenReturn(Mono.error(new RuntimeException("Test Exception")));
        orderConsumer.listen(paymentResponse);

        verify(orderService, times(1)).processOrder(paymentResponse);
    }

}
