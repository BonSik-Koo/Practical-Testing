package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.cafekiosk.spring.domain.product.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllBySellingTypeIn(List<ProductSellingType> sellingTypeList);

}