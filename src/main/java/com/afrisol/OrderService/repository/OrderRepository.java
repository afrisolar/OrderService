
package com.afrisol.OrderService.repository;

import com.afrisol.OrderService.model.CustomerOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface OrderRepository  extends ReactiveCrudRepository<CustomerOrder, Long> {
    Mono<CustomerOrder> findByOrderNumber(String orderNumber);
}

