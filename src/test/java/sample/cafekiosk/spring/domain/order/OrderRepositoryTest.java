package sample.cafekiosk.spring.domain.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired OrderRepository orderRepository;

    @DisplayName("이틀 간의 주문들 중 원하는 주문 상태와 첫째날의 주문 리스트를 조회한다.")
    @Test
    void findOrderBy(){
        //given
        LocalDateTime targetStartDateTime = LocalDate.of(2023, 8, 6).atStartOfDay();
        LocalDateTime targetEndDateTime = LocalDate.of(2023,8,7).atStartOfDay();

        LocalDateTime dateTime1 = LocalDateTime.of(2023, 8,6,0,0); // 경계값
        LocalDateTime dateTime2 = LocalDateTime.of(2023,8,6,20,0);
        LocalDateTime dateTime3 = LocalDateTime.of(2023, 8,7,0,0); // 경계값

        orderRepository.saveAll(
                List.of(
                        createPaymentCompletedOrder(dateTime1),
                        createPaymentCompletedOrder(dateTime2),
                        createPaymentCompletedOrder(dateTime3)
                        )
        );

        //when
        List<Order> orders = orderRepository.findOrderBy(targetStartDateTime, targetEndDateTime, OrderStatus.PAYMENT_COMPLETED);

        //then
        assertThat(orders).hasSize(2)
                .extracting("status", "registeredDateTime")
                .containsExactlyInAnyOrder(
                        tuple(OrderStatus.PAYMENT_COMPLETED, dateTime1),
                        tuple(OrderStatus.PAYMENT_COMPLETED, dateTime2)
                );
     }

     private Order createPaymentCompletedOrder(LocalDateTime registeredDateTime){
        return Order.builder()
                .products(List.of())
                .status(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(registeredDateTime)
                .build();
     }

}