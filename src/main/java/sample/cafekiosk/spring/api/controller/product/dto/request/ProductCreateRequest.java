package sample.cafekiosk.spring.api.controller.product.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    /**
     * TODO: 읽기
     * 기본 validation vs 도메인 validation 를 구분하자!!!!!!!!!!!
     * 기본 validation (String 타입, 비었는지 등)은 컨트롤러에서 잡는게 맞다.(최소한의 검증!!)
     * but) 도메인 validation(길이 등)은 "도메인(생성자 등)" or "서비스"에서 검증하는 걸로 분리해서 "책임"을 분리하는 것이 좋다.!!!
     */
    @NotNull(message = "상품 타입은 필수입니다.")
    private ProductType type;

    @NotNull(message = "상품 판매상태는 필수입니다.")
    private ProductSellingType sellingType;

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;

    @Positive(message = "상품 가격은 양수여야 합니다.")
    private int price;

    @Builder
    private ProductCreateRequest(ProductType type, ProductSellingType sellingType, String name, int price) {
        this.type = type;
        this.sellingType = sellingType;
        this.name = name;
        this.price = price;
    }

    public ProductCreateServiceRequest toServiceRequest() {
        return ProductCreateServiceRequest.builder()
                .type(type)
                .sellingType(sellingType)
                .name(name)
                .price(price)
                .build();
    }

}
