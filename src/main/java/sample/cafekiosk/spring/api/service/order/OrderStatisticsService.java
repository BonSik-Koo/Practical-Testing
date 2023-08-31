package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
/**
 * 메인 전송 등 외부 API 를 쓰는 Service 는 @Transactional 를 붙히지 않는 것이 좋다.
 * 기본적 JPA 구현체에 트랜잭션(읽기, 쓰기)이 있기 때문에 굳이 필요없다.
 * 만약 달게 된다면, 트랜잭션을 획득을 위해 "DB 커넥션"을 계속 연결하고 있기 떄문에, 오래 걸릴 수 있는 외부 API 동안 쓸데 없이 계속 가지고 있게 된다.
 */
public class OrderStatisticsService {

    private final OrderRepository orderRepository;
    private final MailService mailService;

    public boolean sendOrderStatisticsMail(LocalDate orderDate, String email){
        // 해당 일자에 결제 완료된 주문 리스트 조회
        List<Order> orders = orderRepository.findOrderBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED);

        //총 매출 합계 계산
        int totalAmount = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        //메일 전송
        boolean result = mailService.sendMail(
                "no-reply@cafekiosk.com",
                email,
                String.format("[매출 통계] %s", orderDate),
                String.format("총 매출 합계는 %s원입니다.", totalAmount)
        );
        if(!result){
            throw new IllegalArgumentException("매출 통게 매일 전송에 실패했습니다.");
        }

        return true;
    }

}
