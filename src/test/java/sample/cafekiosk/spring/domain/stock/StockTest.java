package sample.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collection;
import java.util.List;

/**
 * TDD 방식
 * 단위 테스트
 */
class StockTest {

    @DisplayName("재고의 수량이 제공된 수량보다 작은지 확인한다.")
    @Test
    void isQuantityLessThan(){
        //given
        Stock stock = Stock.create("001", 1);
        int quantity = 2;

        //when
        boolean result = stock.isQuantityLessThan(2); //경게값 테스트 !!

        //then
        assertThat(result).isTrue();
     }

     @DisplayName("재고를 주어진 개수만큼 차감할 수 있다.")
     @Test
     void deductQuantity1(){
         //given
         Stock stock = Stock.create("001", 1);
         int quantity = 1;

         //when
         stock.deductQuantity(quantity);

         //then
         assertThat(stock.getQuantity()).isZero();
      }

      @DisplayName("재고보다 많은 수의 수량으로 차감을 시도하는 경우 예외가 발생한다.")
      @Test
      void deductQuantity2(){
          //given
          Stock stock = Stock.create("001", 1);
          int quantity = 2;

          //when & then
          assertThatThrownBy(() -> stock.deductQuantity(quantity))
                  .isInstanceOf(IllegalArgumentException.class)
                  .hasMessage("차감할 재고 수량이 없습니다.");
    }
    
    /**
     * @DynamicTest
     * 공유변수를 사용해서 순서에 따라 테스트 작성이 필요한 경우
     */
    @DisplayName("재고 차감 시나리오")
    @TestFactory
    Collection<DynamicTest> stockDeductDynaminTest(){
        //given
        Stock stock = Stock.create("001", 1);
        
        return List.of(
            DynamicTest.dynamicTest("재고를 주어진 개수만큼 차감할 수 있다.", () ->{
                //given
                int quantity = 1;
                
                //when
                stock.deductQuantity(quantity);
                
                //then
                assertThat(stock.getQuantity()).isZero();
            }),
            DynamicTest.dynamicTest("재고보다 많은 수의 수량으로 차감을 시도하는 경우 예외가 발생한다.", () -> {
                //given
                int quantity = 1;
                
                //when & then
                assertThatThrownBy(() -> stock.deductQuantity(quantity))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("차감할 재고 수량이 없습니다.");
            })
        );
     }

}