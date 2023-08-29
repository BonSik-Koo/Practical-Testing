package sample.cafekiosk.spring.domain.stock;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    private int quantity;

    @Builder
    private Stock(String productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }

    public static Stock create(String productNumber, int quantity){
        return Stock.builder()
                .productNumber(productNumber)
                .quantity(quantity)
                .build();
    }

    public boolean isQuantityLessThan(int quantity) {
        return this.quantity < quantity;
    }

    public void deductQuantity(int quantity) {
        /**
         * Service 에서 검증하는데 또 왜? 체크?
         * 이 메서드는 밖에 Service 를 알지 못한다. 그러므로 이 메서드 자체로 성공을 보장해줘야 된다.
         * 혹여나 다른 서비스에서도 사용할 수 있으므로.
         *
         * 그럼 서비스에서 예외는 안둬도 되는거 아니야?
         * 별도의 메시지로 사용해야 하니. 핸들링 상황이 다름.
         */
        if (isQuantityLessThan(quantity)) {
            throw new IllegalArgumentException("차감할 재고 수량이 없습니다.");
        }
        this.quantity -= quantity;
    }

}
