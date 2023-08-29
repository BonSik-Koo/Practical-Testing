package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    /**
     * 재고 감소 -> 동시성 고민
     * optimistic lock / pessimistic lock 등
     */
    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registerDateTime) {
        // Product
        List<String> productNumber = request.getProductNumber();

        //중복 상품도 고려한 상품 엔티티 찾기
        List<Product> products = findProductBy(productNumber);

        deductStockQuantities(products);

        // Order
        Order order = Order.create(products, registerDateTime);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    private List<Product> findProductBy(List<String> productNumber) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumber);
        Map<String, Product> productMap = products.stream()
                .collect(toMap(Product::getProductNumber, p -> p));

        return productNumber.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
    }

    private void deductStockQuantities(List<Product> products) {
        List<String> stockProductNumbers = extractStockProductNumbers(products);

        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
        Map<String, Long> productCounting = createCountingMapBy(stockProductNumbers);

        // 재고 차감 시도
        for(String stockProductNumber : new HashSet<>(stockProductNumbers)){
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCounting.get(stockProductNumber).intValue();

            if(stock.isQuantityLessThan(quantity)){
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다");
            }
            stock.deductQuantity(quantity);
        }
    }

    private static List<String> extractStockProductNumbers(List<Product> products) {
        // 재고 차감 체크가 필요한 상품 filter
        return products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .collect(Collectors.toList());
    }

    private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
        // 재고 엔티티 조회
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        return stocks.stream()
                .collect(toMap(Stock::getProductNumber, s -> s));
    }

    private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        // 상품별 counting
        return stockProductNumbers.stream()
                .collect(groupingBy(p -> p, counting()));
    }

}
